import java.util.concurrent.Semaphore;

public class RaceCondition
{
    //Race Condition Example 
    static int count = 0;
    static Semaphore semaphore = new Semaphore(1);

    private static void doRaceWork()
    {
        for (int i = 0; i < 100000; ++i)
        {
            try
            {
                semaphore.acquire(); // synchronized access
                count++;
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
            finally
            {
                semaphore.release();
            }
        }
    }

    // Checked Exception Example 
    private static void checkedExceptionExample() throws Exception
    {
        throw new Exception("This is a checked exception example.");
    }

    // Unchecked Exception Example 
    private static void uncheckedExceptionExample()
    {
        throw new RuntimeException("This is an unchecked exception example.");
    }

    // Handling Exceptions with try-catch 
    private static void handleWithTryCatch()
    {
        try
        {
            checkedExceptionExample();
        }
        catch (Exception e)
        {
            System.out.println("Handled checked exception: " + e.getMessage());
        }

        try
        {
            uncheckedExceptionExample();
        }
        catch (RuntimeException e)
        {
            System.out.println("Handled unchecked exception: " + e.getMessage());
        }
    }

    // UncaughtExceptionHandler 
    private static void threadWithCustomHandler()
    {
        Thread t = new Thread(() ->
        {
            throw new RuntimeException("Thread-specific uncaught exception!");
        });

        t.setUncaughtExceptionHandler((thread, e) ->
        {
            System.out.println("Custom handler caught: " + e.getMessage());
        });

        t.start();
    }

    // DefaultUncaughtExceptionHandler 
    private static void threadWithDefaultHandler()
    {
        Thread.setDefaultUncaughtExceptionHandler((thread, e) ->
        {
            System.out.println("Default handler caught: " + e.getMessage());
        });

        Thread t = new Thread(() ->
        {
            throw new RuntimeException("Global uncaught exception!");
        });

        t.start();
    }

    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("=== Race Condition Example ===");

        Thread t1 = new Thread(RaceCondition::doRaceWork);
        Thread t2 = new Thread(RaceCondition::doRaceWork);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final count: " + count);

        System.out.println("\n=== Exception Handling Examples ===");
        handleWithTryCatch();

        System.out.println("\n=== UncaughtExceptionHandler Example ===");
        threadWithCustomHandler();

        // Wait to avoid overlapping output
        Thread.sleep(100);

        System.out.println("\n=== DefaultUncaughtExceptionHandler Example ===");
        threadWithDefaultHandler();
    }
}
