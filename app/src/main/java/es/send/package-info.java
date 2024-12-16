/**
 * Implementation of the Bridge pattern for message sending functionality.
 * Provides abstractions and implementations for sending messages through different channels.
 * 
 * <h2>Package Components:</h2>
 * <ul>
 *   <li>{@link es.send.SendAbstraction} - Interface defining the abstraction:
 *     <ul>
 *       <li>Defines high-level message sending operations</li>
 *       <li>Implemented by {@link es.send.SendAbstractionImpl}</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.send.SendImplementor} - Interface for concrete implementations:
 *     <ul>
 *       <li>Defines platform-specific sending operations</li>
 *       <li>Manages source Activity references</li>
 *       <li>Implemented by SMS and WhatsApp concrete classes</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * <h2>Concrete Implementors:</h2>
 * <ul>
 *   <li>{@link es.send.SMSImplementor} - SMS messaging implementation:
 *     <ul>
 *       <li>Uses Android's SMS sending Intent</li>
 *       <li>Handles SMS URI formatting</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.send.WhatsAppImplementor} - WhatsApp messaging implementation:
 *     <ul>
 *       <li>Uses WhatsApp's API URL format</li>
 *       <li>Handles package availability checking</li>
 *       <li>Manages phone number formatting</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * <h2>Usage:</h2>
 * <ul>
 *   <li>Create a {@link es.send.SendAbstractionImpl} instance with desired method ("SMS" or "WhatsApp")</li>
 *   <li>Use the {@code send()} method to send messages through the selected channel</li>
 *   <li>Handles graceful fallback if sending method is unavailable</li>
 * </ul>
 * 
 * <h2>Design Pattern:</h2>
 * <ul>
 *   <li>Implements the Bridge pattern to decouple abstraction from implementation</li>
 *   <li>Allows independent variation of sending methods</li>
 *   <li>Facilitates addition of new sending methods without modifying existing code</li>
 * </ul>
 */
package es.send;