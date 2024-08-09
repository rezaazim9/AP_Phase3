package model;

import model.entities.Ability;
import model.entities.Skill;
import org.apache.commons.lang3.tuple.Triple;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

public abstract class UserInterfaceController {
    private UserInterfaceController(){}




    public static void fireSkill() {
        if (Skill.getActiveSkill() != null) Skill.getActiveSkill().fire();
    }




    /**
     * @return a thread-safe hashmap mapping to every skill category name (as key), a list of triples
     * (as value) of name,cost,acquired status of all skills in that category
     */
    public static ConcurrentMap<String, List<Triple<String,Integer,Boolean>>> getSkillTypesData() {
        return controller.UserInterfaceController.getSkillTypesData();
    }

    /**
     * @return a thread-safe hashmap mapping to every ability name (as key) its activation cost (as value)
     */
    public static ConcurrentMap<String, Integer> getAbilitiesData() {
        return controller.UserInterfaceController.getAbilitiesData();
    }

    public static String getActiveSkill() {
        return controller.UserInterfaceController.getActiveSkill();
    }

    public static void setActiveSkill(String skillName) {
        if (Objects.requireNonNull(findSkill(skillName)).isAcquired()) Skill.setActiveSkill(findSkill(skillName));
    }

    public static boolean purchaseSkill(String skillName) {
        Skill skill = findSkill(skillName);
        if (skill == null) return false;
        if (Profile.getCurrent().getTotalXP() < skill.getCost()) return false;
        Profile.getCurrent().setTotalXP(Profile.getCurrent().getTotalXP() - skill.getCost());
        skill.setAcquired(true);
        return true;
    }
    public static boolean canActiveAbility(String abilityName) {
        Ability ability = findAbility(abilityName);
        if (ability == null) return false;
        return Profile.getCurrent().getCurrentGameXP() >= ability.getCost();
    }
    public static boolean activateAbility(String abilityName) {
        Ability ability = findAbility(abilityName);
        if (ability == null) return false;
        if (Profile.getCurrent().getCurrentGameXP() < ability.getCost()) return false;
        Profile.getCurrent().setCurrentGameXP(Profile.getCurrent().getCurrentGameXP() - ability.getCost());
        ability.getAction().actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, null));
        return true;
    }

    public static Skill findSkill(String name) {
        return controller.UserInterfaceController.findSkill(name);
    }

    public static Ability findAbility(String name) {
        return controller.UserInterfaceController.findAbility(name);
    }


}