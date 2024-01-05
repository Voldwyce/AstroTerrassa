package com.example.astroterrassa.services;

import com.example.astroterrassa.model.User;
import com.example.astroterrassa.DAO.UserRepository;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.nimbusds.jose.shaded.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.astroterrassa.model.Pago;
import com.example.astroterrassa.DAO.PagoRepository;
import com.itextpdf.text.pdf.PdfWriter;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChartService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PagoRepository PagoRepository;

    public Map<String, Long> getChartData(String dataType, String year, String month) {
        switch (dataType) {
            case "Usuarios registrados":
                return getUsersRegisteredData(year, month);
            case "Genero":
                return getUsersByGenderData();
            case "Edad":
                return getUsersByAgeData();
            case "Simpatizantes":
                return getUsersBySimpatizantesData(year, month);
            default:
                throw new IllegalArgumentException("Tipo de datos no soportado: " + dataType);
        }
    }

    public Map<String, Long> getUsersRegisteredData(String year, String month) {
        List<User> users = userRepository.findAll();
        Map<String, Long> userCountsByDate = new HashMap<>();

        // Filtrar los usuarios basándonos en el año y el mes proporcionados
        for (User user : users) {
            if (Objects.isNull(user.getRegisterDt())) {
                continue;
            }
            LocalDate registerDate = user.getRegisterDt();
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

    public Map<String, Long> getUsersBySimpatizantesData(String year, String month) {
        List<Pago> pagos = PagoRepository.findAll();
        Map<String, Long> pagoCountsByDate = new HashMap<>();

        // Filtrar los pagos basándonos en el año y el mes proporcionados
        for (Pago pago : pagos) {
            if (Objects.isNull(pago.getFechaPago())) {
                continue;
            }
            LocalDate fechaPago = LocalDate.from(pago.getFechaPago());
            if (year.equals("all") && (month.equals("all") || month.isEmpty())) {
                // Si se selecciona "all" para el año y el mes, se agrupa por año
                pagoCountsByDate.put(String.valueOf(fechaPago.getYear()), pagoCountsByDate.getOrDefault(String.valueOf(fechaPago.getYear()), 0L) + 1);
            } else if (!year.isEmpty() && month.equals("all")) {
                // Si solo se selecciona un año, se agrupa por mes
                if (String.valueOf(fechaPago.getYear()).equals(year)) {
                    pagoCountsByDate.put(String.valueOf(fechaPago.getMonthValue()), pagoCountsByDate.getOrDefault(String.valueOf(fechaPago.getMonthValue()), 0L) + 1);
                }
            } else if (!year.isEmpty() && !month.isEmpty()) {
                // Si se selecciona un mes y un año, se agrupa por día
                if (String.valueOf(fechaPago.getYear()).equals(year) && String.valueOf(fechaPago.getMonthValue()).equals(month)) {
                    pagoCountsByDate.put(String.valueOf(fechaPago.getDayOfMonth()), pagoCountsByDate.getOrDefault(String.valueOf(fechaPago.getDayOfMonth()), 0L) + 1);
                }
            }
        }

        if (year.equals("all") && (month.equals("all") || month.isEmpty())) {
            // Si se selecciona "all" para el año y el mes, no se necesita completar nada
        } else if (!year.isEmpty() && month.equals("all")) {
            // Si se selecciona un año, se completan todos los meses con valor 0
            for (int i = 1; i <= 12; i++) {
                if (!pagoCountsByDate.containsKey(String.valueOf(i))) {
                    pagoCountsByDate.put(String.valueOf(i), 0L);
                }
            }
        } else if (!year.isEmpty() && !month.isEmpty()) {
            // Si se selecciona un mes, se completan todos los días del mes con valor 0
            int yearValue = Integer.parseInt(year);
            int monthValue = Integer.parseInt(month);
            YearMonth yearMonthObject = YearMonth.of(yearValue, monthValue);
            int daysInMonth = yearMonthObject.lengthOfMonth();

            for (int i = 1; i <= daysInMonth; i++) {
                if (!pagoCountsByDate.containsKey(String.valueOf(i))) {
                    pagoCountsByDate.put(String.valueOf(i), 0L);
                }
            }
        }

        return pagoCountsByDate;
    }

    public Map<String, Long> getUsersByGenderData() {
        List<User> users = userRepository.findAll();

        // Agrupar los usuarios por género y contar cuántos usuarios hay de cada género
        return users.stream()
                .filter(Objects::nonNull)
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

    public Map<String, Long> getUsersByAgeData() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(Objects::nonNull)
                .filter(user -> user.getFecha_nt() != null)
                .collect(Collectors.groupingBy(user -> {
                    LocalDate birthDate = user.getFecha_nt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int age = Period.between(birthDate, LocalDate.now()).getYears();
                    if (age < 18) {
                        return "Menos de 18";
                    } else if (age < 25) {
                        return "18-24";
                    } else if (age < 35) {
                        return "25-34";
                    } else if (age < 45) {
                        return "35-44";
                    } else if (age < 55) {
                        return "45-54";
                    } else if (age < 65) {
                        return "55-64";
                    } else {
                        return "65 o más";
                    }
                }, Collectors.counting()));
    }


    public byte[] generarCsv(String data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);

        Map<String, Object> chartData = new Gson().fromJson(data, Map.class);
        for (Map.Entry<String, Object> entry : chartData.entrySet()) {
            pw.println(entry.getKey() + "," + entry.getValue());
        }

        pw.flush();
        pw.close();

        return baos.toByteArray();
    }
}
