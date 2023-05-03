package jade;

public abstract class Component {
    public GameObject gameObject = null; //it works like in Unity
    public void start(){ // it can be overriden

    }
    public abstract void update(float dt);

}
