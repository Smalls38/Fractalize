import java.util.ArrayList;

public class HashMap<K, V> {
    private int size; //the number elements
    private ArrayList<Entry<K, V>>[] buckets; //array with arraylists of entries
    private int aSize; //the bucket size

    public HashMap(int num) {
        this.size = 0; //no elements, size 0
        aSize = num; //set the initial array size to be this
        this.buckets = new ArrayList[num];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<>();
        }
    }
    //resizes the bucket to double its original size, this basically will never be called
    //since i made it so the buckets is always bigger than the number of B genes
    private void resize(){
        ArrayList[] temp = buckets; //stores the old map temporarily
        aSize = aSize*2; //new size is old size times 2
        this.buckets = new ArrayList[aSize]; //make new array
        for (int i = 0; i < aSize/2; i++) { //put everything back in
            buckets[i] = temp[i];
        }
    }
    public void put(K key, V value) {
        if (size + size / 2 >= buckets.length) { //if number of elements is equal or more than 2/3 size of the bucket
            resize();
        }
        ArrayList<Entry<K, V>> bucket = buckets[key.hashCode() % buckets.length]; //put the entry at the hashcode location
        //checks if the key already exists
        for (int i = 0; i < bucket.size(); i++) {
            if (bucket.get(i).key.equals(key)) {
                bucket.get(i).value = value; //replace old value with new value
                return;
            }
        }
        bucket.add(new Entry<K, V>(key, value));
    }
    public V get(K key){
        ArrayList<Entry<K, V>> bucket = buckets[key.hashCode() % buckets.length]; //put the entry at the hashcode location
        for (int i = 0; i < bucket.size(); i++) {
            if (bucket.get(i).key.equals(key)) {
                return bucket.get(i).value; //return the value
            }
        }
        return null; //return nothing because the key doesn't exist, blame em for the problem
    }


    private class Entry<K, V> {
        K key; //value used to access the needed value
        V value; //the value that is accessed

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}

