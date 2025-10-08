package examples;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomTest501 {
    static class Runner1{
        void execute(){
            for(int i=0; i<10; i++){
                System.out.println("Runner1 #"+i);

            }
        }
    }

    static class Runner2{
        void execute(){
            for(int i=0; i<10; i++){
                System.out.println("Runner2 #"+i);
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() ->{
            Runner1 r1 = new Runner1();
            r1.execute();
        });

        Thread t2 = new Thread(() ->{
            Runner2 r2 = new Runner2();
            r2.execute();
        });

        t1.start();
        t2.start();

        Thread.getAllStackTraces()
                .entrySet().stream()
                //.filter(entry -> entry.getKey().getName().toLowerCase().contains("main"))
                .forEach(e ->{
                    Thread thread = e.getKey();
                    StackTraceElement[] trace = e.getValue();
                    System.out.println("🧵 Thread: " + thread.getName());
                    Arrays.stream(trace).forEach(System.out::println);
                    System.out.println("-----------------------");
                });

        Map<Thread.State, Long> stateCount = Thread.getAllStackTraces().keySet().stream()
                .collect(Collectors.groupingBy(Thread::getState, Collectors.counting()));

        System.out.println(stateCount);

        Thread.getAllStackTraces().keySet().stream()
                .map(Thread::getName)
                .filter(name -> !name.startsWith("Finalizer"))
                .forEach(System.out::println);

        Map<Thread.State, Long> stateCount2 = Thread.getAllStackTraces().keySet().stream()
                .collect(Collectors.groupingBy(Thread::getState, Collectors.counting()));
        System.out.println(stateCount2);

        System.out.println("========Thread Priority Map==========");
        Thread.getAllStackTraces().keySet()
                .stream()
                .collect(Collectors.toMap(Thread::getName, Thread::getPriority))
                .entrySet().stream()
                .forEach(e -> System.out.println(e.getKey() + " " + e.getValue()));
        try{
            t1.join();
            t2.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
