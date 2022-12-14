package facades;

import dtos.MealPlanDTO;
import dtos.UserDTO;
import entities.Meal;
import entities.MealPlan;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MealPlanFacade {

    private static MealPlanFacade instance;
    private static EntityManagerFactory emf;

    private MealPlanFacade() {
    }

    public static MealPlanFacade getMealPlanFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MealPlanFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<MealPlanDTO> getAllMealPlansByUser(String username) {
        List<MealPlanDTO> mealPlanList = new ArrayList<>();
        EntityManager em = getEntityManager();
        TypedQuery<MealPlan> query = em.createQuery("SELECT mp FROM MealPlan mp JOIN mp.users u WHERE u.userName = :user_name", MealPlan.class);
        query.setParameter("user_name", username);
        List<MealPlan> mpList = query.getResultList();
        mpList.forEach(mealPlan -> {
            mealPlanList.add(new MealPlanDTO(mealPlan));
        });
        return mealPlanList;
    }

    public MealPlanDTO createMealPlan(MealPlanDTO mealPlanDTO, String username) {
        EntityManager em = getEntityManager();

        List<User> userList = new ArrayList<>();
        User user = em.find(User.class, username);
        userList.add(user);

        MealPlan newMealPlan = new MealPlan(mealPlanDTO.getId(), mealPlanDTO.getMealPlanName(), userList);

        em.getTransaction().begin();
        em.persist(newMealPlan);
        em.getTransaction().commit();
        em.close();
        return new MealPlanDTO(newMealPlan);
    }

    public MealPlanDTO updateMealPlan (MealPlanDTO mealPlanDTO, String username) {
        EntityManager em = getEntityManager();

        List<User> userList = new ArrayList<>();
        User user = em.find(User.class, username);
        userList.add(user);

        MealPlan mealPlan = em.find(MealPlan.class, mealPlanDTO.getId());
        if(mealPlan == null) {
            throw new EntityNotFoundException("No such meal plan with id: " + mealPlanDTO.getId());
        }

        MealPlan updatedMealPlan = new MealPlan(mealPlanDTO.getId(), mealPlanDTO.getMealPlanName(), userList);

        em.getTransaction().begin();
        em.merge(updatedMealPlan);
        em.getTransaction().commit();
        em.close();
        return new MealPlanDTO(updatedMealPlan);
    }

    public MealPlanDTO deleteMealPlan (Integer mealPlanId) {
        EntityManager em = getEntityManager();

        MealPlan mealPlan = em.find(MealPlan.class, mealPlanId);
        try {

            em.getTransaction().begin();
            em.remove(mealPlan);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new MealPlanDTO(mealPlan);
    }

}
