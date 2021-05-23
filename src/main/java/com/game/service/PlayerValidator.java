package com.game.service;

import com.game.entity.Player;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
public class PlayerValidator implements Validator {

    @Override
    public boolean supports(Class<?> claws) {
        return Player.class.equals(claws);
    }

    @Override
    public void validate(Object o, Errors errors) {

        if (!supports(o.getClass())) {
            errors.reject("Wrong class");
        }

        Player player = (Player) o;

        if (player.getRace() == null ||
                player.getProfession() == null) {
            errors.reject("Not all parameters are specified");
            return;
        }

        if (player.getName().length() > 12 || player.getName().length() == 0) {
            errors.reject("name", "the length of the name cannot be more than 12");
        }

        if (player.getTitle().length() > 30) {
            errors.reject("title", "the length of the ешеду cannot be more than 30");
        }

        if (player.getExperience() < 0 ||
                player.getExperience() > 10_000_000) {
            errors.reject("Experience", "Experience is not valid");
        }

        if (player.getBirthday().before(new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime()) ||
                player.getBirthday().after(new GregorianCalendar(3000, Calendar.DECEMBER, 31).getTime())) {

            errors.rejectValue("birthday", "", "birthday is out of range");
        }
    }
}
