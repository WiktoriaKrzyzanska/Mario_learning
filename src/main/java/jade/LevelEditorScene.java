package jade;

import components.FontRenderer;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import renderer.Shader;
import renderer.Texture;
import util.Time;


import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;


import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{

//   private int vertexID, fragmentID, shaderProgram; //shader to i vertex i program
    private Shader defaultShader;
    private Texture testTexture;
    GameObject testObj;
    private boolean firstTime = false;
    private float[] vertexArray = {
            // position                               // color                      //UV Coordinates
            100.5f, 0.5f,  0.0f,                    1.0f, 0.0f, 0.0f, 1.0f,         1, 1, //Bottom right     0
            0.5f, 100.5f,  0.0f,                    0.0f, 1.0f, 0.0f, 1.0f,         0, 0, //Top left   1
            100.5f,  100.5f,  0.0f,                 1.0f, 0.0f, 1.0f, 1.0f,         1, 0, //Top right 2
            0.5f, 0.5f, 0.0f,                       1.0f, 1.0f, 0.0f, 1.0f,         0, 1 //Bottom left 3


    };
    //Numeracja vertexArray to 0,1,2,3,4 i wykorzystamy to poniżej
    //IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            /*
                    X           X

                    X           X
             */
            2,1,0, //Top right triangle
            0,1,3 //Bottom left triangle
    };
    private int vaoID, vboID, eboID;
    public LevelEditorScene(){

    }
    @Override
    public void init(){
        System.out.println("Creating test object");
        this.testObj = new GameObject("test object");
        this.testObj.addComponent(new SpriteRenderer());
        this.testObj.addComponent(new FontRenderer());
        this.addGameObjectToScene(this.testObj); // adding to the scene
        this.camera = new Camera(new Vector2f());
       defaultShader = new Shader("assets" + File.separator + "shaders" + File.separator + "default.glsl");
       defaultShader.compile();
       this.testTexture = new Texture("assets/images/testImage.png");
        //===================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        //===================================================
        vaoID = glGenVertexArrays(); //it creates an array in GPU
        glBindVertexArray(vaoID); //do everything after this line to this array
        //Create a float buffer of vertices - it is must
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip(); //without flip there will be an error

        //Create VBO to upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID); // what are we binding
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW); // we are working on buffer and we want to send to specific vertex and then we want tp draw statically


        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);


        //Add the vertex attribute pointers
        int positionSize = 3; //three position
        int colorSize = 4; // bo r,g,b,a
        //size of a float is 4 bytes
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES; // calculating vertex size, because Java doesn't do it
        glVertexAttribPointer(0,positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        //skladnia   =        atrybut = 0,size,   jaki typ,  moze byc false/true, how long to next vertex in bytes, pointer which is offset)
        //why 0? in glsl we said location is 0
        glEnableVertexAttribArray(0); //enable
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        //positionSize because we moved position, and it has to be in bytes
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2,uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize+ colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        camera.position.x -= dt * 50.0f;
        camera.position.y -= dt * 20.0f;
        defaultShader.use();

        // Upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0); //upload what we have under ID 0
        glActiveTexture(GL_TEXTURE0); //activating slot 0
        testTexture.bind(); //pushes it into slot 0 in shader

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        //Bind the VAO that we're using
        glBindVertexArray(vaoID);
        //Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0); //0 means binds nothing
        defaultShader.detach();

        if(!firstTime) {
            System.out.println("Creating gameObject");
            GameObject go = new GameObject("Game Test 2");
            go.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(go);
            firstTime = true;
        }
        for (GameObject go : this.gameObjects){
            go.update(dt);
        }
    }
}
