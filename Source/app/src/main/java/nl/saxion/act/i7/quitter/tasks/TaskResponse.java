package nl.saxion.act.i7.quitter.tasks;

public interface TaskResponse<T> {
    void onResponse(T output);
}
