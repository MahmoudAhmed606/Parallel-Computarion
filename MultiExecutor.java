import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MultiExecutor
{
    private final List<Runnable> tasks;

    public MultiExecutor(List<Runnable> tasks)
    {
        this.tasks = tasks;
    }

    public void executeAll()
    {
        List<Thread> threads = new ArrayList<>();

        // Create a thread for each task
        for (Runnable task : tasks)
        {
            Thread thread = new Thread(task);
            threads.add(thread);
        }

        // Start all threads
        for (Thread thread : threads)
        {
            thread.start();
        }
    }

    public static void main(String[] args)
    {
        List<Runnable> tasks = new ArrayList<>();

        // Task 1: Print first 6 Fibonacci numbers
        tasks.add(() ->
        {
            int a = 0, b = 1;
            for (int i = 1; i <= 6; i++)
            {
                System.out.println("Task 1 - Fibonacci #" + i + " = " + b);
                int next = a + b;
                a = b;
                b = next;
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });

        // Task 2: Print current time 5 times (like snapshots)
        tasks.add(() ->
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            for (int i = 1; i <= 5; i++)
            {
                String time = LocalTime.now().format(formatter);
                System.out.println("Task 2 - Time snapshot " + i + " = " + time);
                try
                {
                    Thread.sleep(300);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });

        // Task 3: Simulate a download progress printing percentages
        tasks.add(() ->
        {
            for (int p = 10; p <= 100; p += 10)
            {
                System.out.println("Task 3 - Download progress: " + p + "%");
                try
                {
                    Thread.sleep(150);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });

        MultiExecutor executor = new MultiExecutor(tasks);

        System.out.println("Starting parallel execution of all tasks...\n");

        executor.executeAll();

        System.out.println("\nAll tasks have been started concurrently!");
    }
}
