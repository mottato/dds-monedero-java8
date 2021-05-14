package dds.monedero.model;
import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

/* Code Smell 1: Las validaciones podrían estar delegadas en una clase que solo se encargue de eso, ya que
validar los montos no es responsabilidad de la cuenta.
 */
public class Validaciones {

    public static void validarMontoNegativo(double monto){
        if (monto<= 0) {
            throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
        }
    }
    public static void validarMaximaCantidadDeDepositos(Cuenta cuenta){
        if (cuenta.getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
            throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
        }
    }
    public static void validarSaldoMenorAMonto(double monto, double saldo){
        if (saldo - monto < 0) {
            throw new SaldoMenorException("No puede sacar mas de " + saldo + " $");
        }
    }
    public static void validarMaximaExtraccionDiaria(double monto, double limite){
        if (monto > limite) {
            throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
                    + " diarios, límite: " + limite);
        }
    }
}
