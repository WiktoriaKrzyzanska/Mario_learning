package components;

import jade.Component;

public class FontRenderer extends Component {
    public FontRenderer(){}
    @Override
    public void start(){
        if(gameObject.getComponent(SpriteRenderer.class) != null){
            System.out.println("Found a font renderer!");
        }
    }
    @Override
    public void update(float dt) {

    }
}
