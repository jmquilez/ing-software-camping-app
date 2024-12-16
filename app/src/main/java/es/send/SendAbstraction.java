package es.send;

/** Define la interfaz de la abstracción */
public interface SendAbstraction {

	/** Definición del metodo que permite realizar el envío del mensaje con texto 'message'
	 * @param phone número de teléfono del destinatario
	 * @param message cuerpo del mensaje a enviar
	 */
	public void send(String phone, String message);
}
