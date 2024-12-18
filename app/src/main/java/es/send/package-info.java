/**
 * Implementación del patrón Bridge para la funcionalidad de envío de mensajes.
 * Proporciona abstracciones e implementaciones para enviar mensajes a través de diferentes canales.
 * 
 * <h2>Componentes del Paquete:</h2>
 * <ul>
 *   <li>{@link es.send.SendAbstraction} - Interfaz que define la abstracción:
 *     <ul>
 *       <li>Define operaciones de alto nivel para envío de mensajes</li>
 *       <li>Implementada por {@link es.send.SendAbstractionImpl}</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.send.SendImplementor} - Interfaz para implementaciones concretas:
 *     <ul>
 *       <li>Define operaciones específicas de la plataforma</li>
 *       <li>Gestiona referencias a la Activity origen</li>
 *       <li>Implementada por las clases concretas de SMS y WhatsApp</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * <h2>Implementadores Concretos:</h2>
 * <ul>
 *   <li>{@link es.send.SMSImplementor} - Implementación de mensajería SMS:
 *     <ul>
 *       <li>Utiliza el Intent de envío de SMS de Android</li>
 *       <li>Maneja el formateo de URIs para SMS</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.send.WhatsAppImplementor} - Implementación de mensajería WhatsApp:
 *     <ul>
 *       <li>Utiliza el formato de URL de la API de WhatsApp</li>
 *       <li>Verifica la disponibilidad del paquete</li>
 *       <li>Gestiona el formateo de números de teléfono</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * <h2>Uso:</h2>
 * <ul>
 *   <li>Crear una instancia de {@link es.send.SendAbstractionImpl} con el método deseado ("SMS" o "WhatsApp")</li>
 *   <li>Usar el método {@code send()} para enviar mensajes a través del canal seleccionado</li>
 *   <li>Maneja la recuperación elegante si el método de envío no está disponible</li>
 * </ul>
 * 
 * <h2>Patrón de Diseño:</h2>
 * <ul>
 *   <li>Implementa el patrón Bridge para desacoplar la abstracción de la implementación</li>
 *   <li>Permite la variación independiente de los métodos de envío</li>
 *   <li>Facilita la adición de nuevos métodos de envío sin modificar el código existente</li>
 * </ul>
 */
package es.send;