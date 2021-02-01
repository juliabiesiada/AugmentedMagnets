package fr.ups.wikitude.rendering;

public abstract class Renderable {
    public float[] projectionMatrix = null;
    public float[] viewMatrix = null;

    public abstract void onSurfaceCreated();
    public abstract void onDrawFrame();
}
