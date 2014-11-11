package de.msmobileapps.threadingreference.cfportsources.multiprocessingservices;

/**
 * Defines the entry point of a task.
 */
public interface TaskProc {
    /**
     * @param parameter A pointer to the application-defined value you passed to the function MPCreateTask. For
     *                  example, this value could point to a data structure or a memory location.
     * @return
     */
    public OSStatus execute(Object parameter);
}
