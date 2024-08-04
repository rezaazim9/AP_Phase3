package model;

import model.characters.EpsilonModel;
import model.characters.GeoShapeModel;
import model.collision.Collidable;
import model.entities.Ability;
import model.entities.Skill;
import model.movement.Movable;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static model.Utils.*;
import static model.characters.GeoShapeModel.allShapeModelsList;

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
        ConcurrentMap<String, List<Triple<String,Integer,Boolean>>> out = new ConcurrentHashMap<>();
        for (Skill.SkillType type : Skill.SkillType.values()) {
            List<Triple<String,Integer,Boolean>> skills = new CopyOnWriteArrayList<>();
            for (Skill skill : Skill.values()) {
                if (skill.getType()==type)  skills.add(new MutableTriple<>(skill.getName(),skill.getCost(), skill.isAcquired()));
            }
            out.put(type.name(),skills);
        }
        return out;
    }

    /**
     * @return a thread-safe hashmap mapping to every ability name (as key) its activation cost (as value)
     */
    public static ConcurrentMap<String, Integer> getAbilitiesData() {
        ConcurrentHashMap<String, Integer> out = new ConcurrentHashMap<>();
        for (Ability ability : Ability.values()) out.put(ability.getName(), ability.getCost());
        return out;
    }

    public static String getActiveSkill() {
        if (Skill.getActiveSkill() == null) return null;
        return Skill.getActiveSkill().getName();
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
        for (Skill.SkillType type : Skill.SkillType.values()) {
            for (Skill skill : Skill.values()) {
                if (skill.getType()==type && skill.getName().equals(name)) return skill;
            }
        }
        return null;
    }

    public static Ability findAbility(String name) {
        for (Ability ability : Ability.values()) if (ability.getName().equals(name)) return ability;
        return null;
    }


}