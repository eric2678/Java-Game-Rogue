package src.KeyProcess;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static src.KeyProcess.ObjectDisplayGrid.endControl;

public class KeyStrokePrinter implements InputObserver, Runnable {

    private static int DEBUG = 0;
    private static String CLASSID = "KeyStrokePrinter";
    private static Queue<Character> inputQueue = null;
    private ObjectDisplayGrid displayGrid;
    private Object move;

    public static String[][] map;

    public KeyStrokePrinter(ObjectDisplayGrid grid, Object _move) {
        inputQueue = new ConcurrentLinkedQueue<>();
        displayGrid = grid;
        move = _move;
    }

    @Override
    public void observerUpdate(char ch) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".observerUpdate receiving character " + ch);
        }
        inputQueue.add(ch);
    }

    private void rest() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean processInput() {

        char ch;
        boolean processing = true;
        while (processing) {
            if (inputQueue.peek() == null) {
                // System.out.println("1");
                processing = false;
            } else {
                ch = inputQueue.poll();
                // System.out.println("=======================");
                if (DEBUG > 1) {
                    System.out.println(CLASSID + ".processInput peek is " + ch);
                }
                if (ch == 'X') {
                    System.out.println("got an X, ending input checking");
                    return false;
                } else {
                    // System.out.println("character " + ch + " entered on the keyboard");
                }
            }
        }
        return true;
    }

    @Override
    public void run() { 
        displayGrid.registerInputObserver(this);
        boolean working = true;
        while (working) {
            // System.out.println("Key");
            if(!Thread.currentThread().isInterrupted() && endControl){
                System.out.println("Name of thread: " + Thread.currentThread().getName());
                return;
            }
            rest();
            working = (processInput( ));
        }  
    }
}
