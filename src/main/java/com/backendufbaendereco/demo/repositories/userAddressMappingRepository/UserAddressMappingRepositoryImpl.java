package com.backendufbaendereco.demo.repositories.userAddressMappingRepository;

import com.backendufbaendereco.demo.entities.address.Address;
import com.backendufbaendereco.demo.entities.user.User;
import com.backendufbaendereco.demo.entities.user.UserAddressMapping;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Getter
@Repository
public class UserAddressMappingRepositoryImpl implements UserAddressMappingRepository {
    @PersistenceContext // or even @Autowired
    private EntityManager entityManager;

    @Override
    public void save(UserAddressMapping userAddressMapping) {
        EntityManager em = getEntityManager();
        em.persist(userAddressMapping);
    }

    @Override
    public void deleteById(Long id) {
        EntityManager em = getEntityManager();
        em.createQuery("delete from UserAddressMapping uam where uam.id = :id")
                .setParameter("id", id).executeUpdate();
    }

    @Override

    public void updateIsMainAddressByUserId(User user) {
        EntityManager em = getEntityManager();

        em.createQuery("UPDATE UserAddressMapping  SET isMainAddress = false WHERE userId = :userId")
                .setParameter("userId", user).executeUpdate();


    }

    @Override
    public Optional<UserAddressMapping> findByUserIdAndByAddressId(User userId, Address addressId) {
        EntityManager em = getEntityManager();
        try {
            UserAddressMapping resultList = em.createQuery(
                            "SELECT uam FROM UserAddressMapping uam " +
                                    "INNER JOIN FETCH uam.addressId addr " +
                                    "INNER JOIN FETCH addr.cityId c " +
                                    "INNER JOIN FETCH addr.stateId s " +
                                    "WHERE uam.userId = :user " +
                                    "AND uam.addressId = :address ", UserAddressMapping.class)
                    .setParameter("user", userId)
                    .setParameter("address", addressId)
                    .getSingleResult();

            return Optional.of(resultList);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<UserAddressMapping> findAllUserAddress(Pageable pageable, Long userId) {
        EntityManager em = getEntityManager();
        TypedQuery<UserAddressMapping> query = em.createQuery(
                        "SELECT uam FROM UserAddressMapping uam " +
                                "INNER JOIN FETCH uam.addressId addr " +
                                "INNER JOIN FETCH addr.cityId c " +
                                "INNER JOIN FETCH addr.stateId s " +
                                "WHERE uam.userId.id = :user ", UserAddressMapping.class)
                .setParameter("user", userId).setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        List<UserAddressMapping> resultList = query.getResultList();

        return new PageImpl<>(resultList, pageable, resultList.size());
    }

    @Override
    public Optional<UserAddressMapping> findByAddressIdUserAddress(Long userId, Long addressId) {
        EntityManager em = getEntityManager();
        try {
            UserAddressMapping resultList = em.createQuery(
                            "SELECT uam FROM UserAddressMapping uam " +
                                    "INNER JOIN FETCH uam.addressId addr " +
                                    "INNER JOIN FETCH addr.cityId c " +
                                    "INNER JOIN FETCH addr.stateId s " +
                                    "WHERE uam.userId.id = :user " +
                                    "AND uam.addressId.id = :address ", UserAddressMapping.class)
                    .setParameter("user", userId)
                    .setParameter("address", addressId)
                    .getSingleResult();

            return Optional.of(resultList);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }


}
