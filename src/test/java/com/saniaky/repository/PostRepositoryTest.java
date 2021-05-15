package com.saniaky.repository;

import com.saniaky.entity.Post;
import com.saniaky.entity.translation.StatusTranslation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class PostRepositoryTest {

    @Autowired
    private PostRepository repository;

    @Autowired
    private EntityManager em;

    @Test
    void testRepo() {
        List<Post> posts = repository.findAll();
        assertEquals(4, posts.size());
        assertEquals(UUID.fromString("ecb3041e-52f1-473f-bce3-736b8dbe1975"), posts.get(0).getId());
        assertEquals("PENDING", posts.get(0).getStatusName());
    }

    /**
     * Use OneToMany annotation with left outer join using tuple.
     */
    @Test
    void testOneToManyAssociationWithTuple() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
        Root<Post> post = cq.from(Post.class);

        String locale = "ru-RU";
        String searchValue = "%обр%";
        ParameterExpression<String> pLocale = cb.parameter(String.class);
        ParameterExpression<String> pValue = cb.parameter(String.class);

        Join<Post, StatusTranslation> join = post.join("statusTranslations", JoinType.LEFT);
        Predicate lang = cb.equal(join.get("id").get("locale"), pLocale);
        Predicate value = cb.like(join.get("translation"), pValue);
        join.on(cb.and(lang));

        cq.multiselect(post, join.get("translation"));
        cq.where(value);
        cq.orderBy(cb.desc(join.get("translation")));

        List<Tuple> list = em.createQuery(cq)
                .setParameter(pLocale, locale)
                .setParameter(pValue, searchValue)
                .getResultList();

        assertEquals(1, list.size());
        Post post1 = list.get(0).get(post);
        post1.setStatusName(list.get(0).get(join.get("translation")));
        assertEquals(UUID.fromString("ecb3041e-52f1-473f-bce3-736b8dbe1975"), post1.getId());
        assertEquals("В обработке", post1.getStatusName());
        // triggers new selects, don't use it: list.get(0).getStatusTranslations()
    }

    /**
     * No need in entity mappings, behaves like an inner join.
     */
    @Test
    void testOneToManyViewCrossJoin() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);

        Root<Post> post = cq.from(Post.class);
        Root<StatusTranslation> statusTranslation = cq.from(StatusTranslation.class);

        String locale = "ru-RU";
        String searchValue = "%обр%";
        int limit = 100;
        int offset = 0;

        Predicate postRestriction = cb.and();
        Predicate translationRestriction = cb.and(
                cb.equal(statusTranslation.get("id").get("code"), post.get("status")),
                cb.equal(statusTranslation.get("id").get("locale"), locale),
                cb.like(statusTranslation.get("translation"), searchValue)
        );

        cq.multiselect(post, statusTranslation);
        cq.where(cb.and(postRestriction, translationRestriction));
        cq.orderBy(cb.desc(statusTranslation.get("translation")));

        List<Tuple> list = em.createQuery(cq)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();

        // Verify
        assertEquals(1, list.size());
        Post postRes = list.get(0).get(post);
        StatusTranslation translationRes = list.get(0).get(statusTranslation);
        postRes.setStatusName(translationRes.getTranslation());
        assertEquals(UUID.fromString("ecb3041e-52f1-473f-bce3-736b8dbe1975"), postRes.getId());
        assertEquals("В обработке", postRes.getStatusName());
        assertEquals("В обработке", translationRes.getTranslation());
    }

    /**
     * Use OneToMany annotation with left outer join using Post entity.
     * Triggers additional selects?!
     */
    @Test
    void testOneToManyAssociationWithPost() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> post = cq.from(Post.class);

        String locale = "ru-RU";
        String searchValue = "%обр%";
        ParameterExpression<String> pLocale = cb.parameter(String.class);
        ParameterExpression<String> pValue = cb.parameter(String.class);

        Join<Post, StatusTranslation> join = post.join("statusTranslations", JoinType.LEFT);
        Predicate lang = cb.equal(join.get("id").get("locale"), pLocale);
        Predicate value = cb.like(join.get("translation"), pValue);
        join.on(cb.and(lang));

        cq.multiselect(post, join);
        cq.where(value);
        cq.orderBy(cb.desc(join.get("translation")));

        List<Post> posts = em.createQuery(cq)
                .setParameter(pLocale, locale)
                .setParameter(pValue, searchValue)
                .getResultList();

        assertEquals(1, posts.size());
        assertEquals(UUID.fromString("ecb3041e-52f1-473f-bce3-736b8dbe1975"), posts.get(0).getId());
        assertEquals("В обработке", posts.get(0).getStatusName());
        // triggers new selects, don't use it: list.get(0).getStatusTranslations()
    }

}
