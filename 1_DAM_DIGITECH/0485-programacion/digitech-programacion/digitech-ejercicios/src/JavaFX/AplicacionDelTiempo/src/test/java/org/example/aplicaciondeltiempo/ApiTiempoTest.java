package org.example.aplicaciondeltiempo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApiTiempoTest {

    @Test
    void consultarTemperatura() {
        Tiempo tiempo = new Tiempo(25.5, 60, 15.0, 1);

        assertEquals(25.5, tiempo.getTemperatura());
    }

    @Test
    void consultarHumedadYCodigo() {
        Tiempo tiempo = new Tiempo(20.0, 75, 10.5, 3);

        assertEquals(75, tiempo.getHumedad());
        assertEquals(3, tiempo.getWeatherCode());
    }

    @Test
    void tiempoNoNulo() {
        try {
            ApiTiempo servicio = new ApiTiempo();
            Tiempo tiempo = servicio.servicioApi("Madrid");

            assertNotNull(tiempo);
        } catch (Exception e) {
            System.out.println("Error de api: " + e.getMessage());
        }
    }

    @Test
    void datosValidosApi() {
        try {
            ApiTiempo servicio = new ApiTiempo();
            Tiempo tiempo = servicio.servicioApi("Barcelona");

            assertTrue(tiempo.getHumedad() >= 0);
        } catch (Exception e) {
            System.out.println("Error de api: " + e.getMessage());
        }
    }

    @Test
    void ciudadIncorrecta() {
        ApiTiempo servicio = new ApiTiempo();

        assertThrows(Exception.class, () -> {
            servicio.servicioApi("aksmdangananfoianfás");
        });
    }
}