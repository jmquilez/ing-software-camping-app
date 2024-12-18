/**
 * ViewModels para la gestión del estado y la lógica de negocio de la interfaz de usuario.
 * 
 * <h2>ViewModels Disponibles:</h2>
 * <ul>
 *   <li>{@link es.unizar.eina.T213_camping.ui.view_models.ParcelaViewModel} - Gestión de parcelas:
 *     <ul>
 *       <li>Operaciones CRUD de parcelas</li>
 *       <li>Consulta de disponibilidad</li>
 *       <li>Gestión de parcelas por reserva</li>
 *       <li>Verificación de existencia</li>
 *       <li>Actualización con cambio de nombre</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.unizar.eina.T213_camping.ui.view_models.ReservaViewModel} - Gestión de reservas:
 *     <ul>
 *       <li>Operaciones CRUD de reservas</li>
 *       <li>Gestión del ciclo de vida de reservas</li>
 *       <li>Verificación de existencia</li>
 *       <li>Operaciones de limpieza y mantenimiento</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.unizar.eina.T213_camping.ui.view_models.ParcelaReservadaViewModel} - Gestión de relaciones parcela-reserva:
 *     <ul>
 *       <li>Gestión de ocupación de parcelas</li>
 *       <li>Consulta de disponibilidad por fechas</li>
 *       <li>Actualización de parcelas por reserva</li>
 *       <li>Verificación de relaciones existentes</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * <h2>Características:</h2>
 * <ul>
 *   <li>Implementación del patrón MVVM</li>
 *   <li>Gestión del ciclo de vida de datos</li>
 *   <li>Uso de LiveData para actualizaciones reactivas</li>
 *   <li>Manejo de operaciones asíncronas</li>
 *   <li>Separación de responsabilidades UI/lógica</li>
 * </ul>
 * 
 * <h2>Integración con Repositorios:</h2>
 * <ul>
 *   <li>Comunicación con {@link es.unizar.eina.T213_camping.database.repositories.ParcelaRepository}</li>
 *   <li>Comunicación con {@link es.unizar.eina.T213_camping.database.repositories.ReservaRepository}</li>
 *   <li>Comunicación con {@link es.unizar.eina.T213_camping.database.repositories.ParcelaReservadaRepository}</li>
 *   <li>Transformación de datos para la UI</li>
 *   <li>Gestión de estados de carga y error</li>
 * </ul>
 */
package es.unizar.eina.T213_camping.ui.view_models; 