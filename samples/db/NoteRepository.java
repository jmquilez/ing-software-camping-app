package es.unizar.eina.notepad.database;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Clase que gestiona el acceso la fuente de datos.
 * Interacciona con la base de datos a través de las clases NoteRoomDatabase y NoteDao.
 */
public class NoteRepository {

    private final NoteDao mNoteDao;
    private final LiveData<List<Note>> mAllNotes;

    private final long TIMEOUT = 15000;

    /**
     * Constructor de NoteRepository utilizando el contexto de la aplicación para instanciar la base de datos.
     * Alternativamente, se podría estudiar la instanciación del repositorio con una referencia a la base de datos
     * siguiendo el ejemplo de
     * <a href="https://github.com/android/architecture-components-samples/blob/main/BasicSample/app/src/main/java/com/example/android/persistence/DataRepository.java">architecture-components-samples/.../persistence/DataRepository</a>
     */
    public NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
        mAllNotes = mNoteDao.getOrderedNotes();
    }

    /** Devuelve un objeto de tipo LiveData con todas las notas.
     * Room ejecuta todas las consultas en un hilo separado.
     * El objeto LiveData notifica a los observadores cuando los datos cambian.
     */
    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    /** Inserta una nota nueva en la base de datos
     * @param note La nota consta de: un título (note.getTitle()) no nulo (note.getTitle()!=null) y no vacío
     *             (note.getTitle().length()>0); y un cuerpo (note.getBody()) no nulo.
     * @return Si la nota se ha insertado correctamente, devuelve el identificador de la nota que se ha creado. En caso
     *         contrario, devuelve -1 para indicar el fallo.
     */
    public long insert(Note note) {
        /* Para que la App funcione correctamente y no lance una excepción, la modificación de la
         * base de datos se debe lanzar en un hilo de ejecución separado
         * (databaseWriteExecutor.submit). Para poder sincronizar la recuperación del resultado
         * devuelto por la base de datos, se puede utilizar un Future.
         */
        Future<Long> future = NoteRoomDatabase.databaseWriteExecutor.submit(
                () -> mNoteDao.insert(note));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("NoteRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    /** Actualiza una nota en la base de datos
     * @param note La nota que se desea actualizar y que consta de: un identificador (note.getId()) mayor que 0; un
     *             título (note.getTitle()) no nulo y no vacío; y un cuerpo (note.getBody()) no nulo.
     * @return Un valor entero con el número de filas modificadas: 1 si el identificador se corresponde con una nota
     *         previamente insertada; 0 si no existe previamente una nota con ese identificador, o hay algún problema
     *         con los atributos.
     */
    public int update(Note note) {
        Future<Integer> future = NoteRoomDatabase.databaseWriteExecutor.submit(
                () -> mNoteDao.update(note));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("NoteRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }


    /** Elimina una nota en la base de datos.
     * @param note Objeto nota cuyo atributo identificador (note.getId()) contiene la clave primaria de la nota que se
     *             va a eliminar de la base de datos. Se debe cumplir: note.getId() > 0.
     * @return Un valor entero con el número de filas eliminadas: 1 si el identificador se corresponde con una nota
     *         previamente insertada; 0 si no existe previamente una nota con ese identificador o el identificador no es
     *         un valor aceptable.
     */
    public int delete(Note note) {
        Future<Integer> future = NoteRoomDatabase.databaseWriteExecutor.submit(
                () -> mNoteDao.delete(note));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("NoteRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }
}
