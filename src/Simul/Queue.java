package Simul;

import java.util.LinkedList;

class Queue extends LinkedList 
{
    
    public void enqueue(Object o) {
        add(o);        
    }
    
    public Object dequeue() {
        return removeFirst();
    }
}// Queue