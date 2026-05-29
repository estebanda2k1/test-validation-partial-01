package ec.edu.epn.skyroute.service;

import ec.edu.epn.SkyRouteApplication;
import org.springframework.stereotype.Service;

/**
 * Calcula las tarifas de equipaje para la aerolínea SkyRoute Airlines.
 * <p>
 * Reglas de negocio:
 * <ol>
 *   <li>Tarifa base: $30.0 por maleta.</li>
 *   <li>Exceso de peso: +$50.0 si una maleta pesa más de 23 kg.</li>
 *   <li>Beneficio VIP: primera maleta gratis si el pasajero es VIP
 *       y la maleta no excede 23 kg.</li>
 *   <li>Excepciones: weight ≤ 0, bagCount < 1, o passengerId nulo
 *       lanzan IllegalArgumentException.</li>
 * </ol>
 */
@Service
public class BaggageFeeCalculator {

    private final SkyRouteApplication skyRouteApplication;
    private final PassengerService passengerService;

    public BaggageFeeCalculator(PassengerService passengerService, SkyRouteApplication skyRouteApplication) {
        this.passengerService = passengerService;
        this.skyRouteApplication = skyRouteApplication;
    }
    /**
     * Calcula la tarifa total de equipaje.
     *
     * @param weight       peso de cada maleta (kg)
     * @param bagCount     cantidad de maletas
     * @param passengerId  identificador del pasajero
     * @return costo total en dólares
     * @throws IllegalArgumentException si los parámetros no cumplen las restricciones
     */
    public double calculateFee(double weight, int bagCount, Long passengerId) {
        if (weight <= 0 || bagCount <1 || passengerId == null) {
            throw new IllegalArgumentException("Parametros del equipaje invalidos");
        }

        boolean isVip= passengerService.isVip(passengerId);
        double total=0.0;
        for  (int i=1; i<=bagCount; i++) {
            boolean firstBagVipFree = isVip && i == 1 && weight <= 23.0;
            if (firstBagVipFree) {
                total += 0.0;
            } else {
                total += 30.0;
                if (weight > 23.0) {
                    total += 50.0;
                }
            }
        }
        return total;
    }
}
