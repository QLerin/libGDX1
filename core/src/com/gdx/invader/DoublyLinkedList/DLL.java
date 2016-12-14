package com.gdx.invader.DoublyLinkedList;

/**
 * Created by Apelsinas Jr on 2014.12.08.
 */
public class DLL<E extends Comparable<E>> {

        private Node<E> first;
        private Node<E> last;
        private Node<E> current;
        private int size;

        public DLL() {
        }

        public boolean add(E e) {
            if(e == null) return false;
            if (first == null) {
                first = new Node<E>(e, null, null);
                last = first;
            } else {
                Node <E> previous = first.findNode(size-1);
                Node<E> e1 = new Node(e, null, previous);
                last.next = e1;
                last = e1;
            }
            size++;
            return true;
        }

        public boolean add(int k, E e){
            if(e == null) return false;
            if (k<0 || k>=size)return false;

            if (k !=0) {
                Node <E> previous = first.findNode(k-1);
                Node <E> next = previous.next;
                previous.next = new Node(e, next, previous);
                size++;
            }
            else {
                Node <E> next = first.findNode(k);
                first = new Node(e, next, null);
                size++;
            }
            return true;
        }

        public int size() {     // grąžinamas sąrašo dydis (elementų kiekis)
            return size;
        }

        public boolean isEmpty() {      // patikrina ar sąrašas yra tuščias
            return first == null;
        }

        public void clear() {
            size = 0;
            first = null;
            last = null;
            current = null;
        }

        public E get(int k){
            if (k<0||k>=size)return null;
            current=first.findNode(k);
            return current.element;
        }

        public E set(int k, E e){
            if(e == null) return null;
            if (k<0 || k>=size)return null;
            current = first.findNode(k);
            current= new Node(e, current.next, current.previous);
            return current.element;
        }

        public E getNext(){
            if(current == null) return null;
            return current.element;
        }

        public E remove(int k) {
            if (k < 0 || k >= size) return null;
            if (k == 0) {
                E deleted = first.element;
                Node <E> next = first.findNode(1);
                first = next;
                size--;
                return deleted;
            }
            if (k == size) {
                E deleted = first.findNode(k).element;
                Node<E> previous = first.findNode(k - 1);
                previous.next = null;
                size--;
                return deleted;
            }
            else
            {
                E deleted = first.findNode(k).element;
                Node<E> previous = first.findNode(k - 1);
                Node <E> next = first.findNode(k+1);
                previous.next = next;
                next.previous = previous;
                size--;
                return deleted;
            }
        }



        private static class Node<E> {
            private E element;
            private Node<E> next;
            private Node<E> previous;

            Node(E data, Node<E> next, Node<E> previous) {
                this.element = data;
                this.next = next;
                this.previous = previous;
            }
            public Node<E> findNode(int k){
                Node<E> e = this;
                for(int i=0; i<k; i++)
                    e=e.next;
                return e;
            }
        }
    }

