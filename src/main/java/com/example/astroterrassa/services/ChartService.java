package com.example.astroterrassa.services;

import com.example.astroterrassa.model.User;
import com.example.astroterrassa.DAO.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChartService {

    @Autowired
    private UserRepository userRepository;

    public Map<String, Long> getChartData(String dataType, String year, String month) {
        switch (dataType) {
            case "Usuarios registrados":
                return getUsersRegisteredData(year, month);
            case "Genero":
                return getUsersByGenderData();
            // Agrega aquí más casos según los tipos de datos que quieras soportar
            default:
                throw new IllegalArgumentException("Tipo de datos no soportado: " + dataType);
        }
    }

    public Map<String, Long> getUsersRegisteredData(String year, String month) {
        List<User> users = userRepository.findAll();
        Map<String, Long> userCountsByDate = new HashMap<>();

        // Filtrar los usuarios basándonos en el año y el mes proporcionados
        for (User user : users) {
            LocalDate registerDate = user.getRegister_dt();
            if (year.equals("all") && (month.equals("all") || month.isEmpty())) {
                // Si se selecciona "all" para el año y el mes, se agrupa por año
                userCountsByDate.put(String.valueOf(registerDate.getYear()), userCountsByDate.getOrDefault(String.valueOf(registerDate.getYear()), 0L) + 1);
            } else if (!year.isEmpty() && month.equals("all")) {
                // Si solo se selecciona un año, se agrupa por mes
                if (String.valueOf(registerDate.getYear()).equals(year)) {
                    userCountsByDate.put(String.valueOf(registerDate.getMonthValue()), userCountsByDate.getOrDefault(String.valueOf(registerDate.getMonthValue()), 0L) + 1);
                }
            } else if (!year.isEmpty() && !month.isEmpty()) {
                // Si se selecciona un mes y un año, se agrupa por día
                if (String.valueOf(registerDate.getYear()).equals(year) && String.valueOf(registerDate.getMonthValue()).equals(month)) {
                    userCountsByDate.put(String.valueOf(registerDate.getDayOfMonth()), userCountsByDate.getOrDefault(String.valueOf(registerDate.getDayOfMonth()), 0L) + 1);
                }
            }
        }

        if (year.equals("all") && (month.equals("all") || month.isEmpty())) {
            // Si se selecciona "all" para el año y el mes, no se necesita completar nada
        } else if (!year.isEmpty() && month.equals("all")) {
            // Si se selecciona un año, se completan todos los meses con valor 0
            for (int i = 1; i <= 12; i++) {
                if (!userCountsByDate.containsKey(String.valueOf(i))) {
                    userCountsByDate.put(String.valueOf(i), 0L);
                }
            }
        } else if (!year.isEmpty() && !month.isEmpty()) {
            // Si se selecciona un mes, se completan todos los días del mes con valor 0
            int yearValue = Integer.parseInt(year);
            int monthValue = Integer.parseInt(month);
            YearMonth yearMonthObject = YearMonth.of(yearValue, monthValue);
            int daysInMonth = yearMonthObject.lengthOfMonth();

            for (int i = 1; i <= daysInMonth; i++) {
                if (!userCountsByDate.containsKey(String.valueOf(i))) {
                    userCountsByDate.put(String.valueOf(i), 0L);
                }
            }
        }

        return userCountsByDate;
    }

    public Map<String, Long> getUsersByGenderData() {
        List<User> users = userRepository.findAll();

        // Agrupar los usuarios por género y contar cuántos usuarios hay de cada género
        return users.stream()
                .collect(Collectors.groupingBy(user -> {
                    switch (user.getGenero()) {
                        case 0:
                            return "Otros";
                        case 1:
                            return "Hombre";
                        case 2:
                            return "Mujer";
                        default:
                            return "Desconocido";
                    }
                }, Collectors.counting()));
    }

}
