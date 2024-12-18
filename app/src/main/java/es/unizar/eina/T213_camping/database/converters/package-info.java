/**
 * Conversores para la base de datos Room.
 * 
 * <h2>Conversores disponibles:</h2>
 * <ul>
 *   <li>{@link es.unizar.eina.T213_camping.database.converters.DateConverter} - Conversión de fechas:
 *     <ul>
 *       <li>Conversión entre {@link java.util.Date} y {@link java.lang.Long}</li>
 *       <li>Almacenamiento de fechas como timestamps</li>
 *       <li>Recuperación de fechas desde timestamps</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * <h2>Uso:</h2>
 * <ul>
 *   <li>Anotados con {@code @TypeConverter} para uso automático por Room</li>
 *   <li>Permiten almacenar tipos complejos en la base de datos SQLite</li>
 *   <li>Utilizados en entidades que manejan fechas</li>
 * </ul>
 */
package es.unizar.eina.T213_camping.database.converters;