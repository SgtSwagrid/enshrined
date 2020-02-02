package game.entity;

import game.math.Matrix4;

public class Transform extends Component {
    
    private Matrix4 transf = Matrix4.IDENTITY;

    protected Transform(Entity entity) {
        super(entity);
        // TODO Auto-generated constructor stub
    }
    
    public void transfRel(Matrix4 mat) {
        transf = transf.mul(mat);
    }
    
    public void transfAbs(Matrix4 mat) {
        transf = mat.mul(transf);
    }
    
    public void setTransf(Matrix4 mat) {
        transf = mat;
    }
    
    public Matrix4 getTransf() {
        return transf;
    }
    
    public float getX() { return transf.asArray()[0][3]; }
    public float getY() { return transf.asArray()[1][3]; }
    public float getZ() { return transf.asArray()[2][3]; }
    
    public float getWidth() {
        float[][] m = transf.asArray();
        return (float)Math.sqrt(m[0][0]*m[0][0] + m[1][0]*m[1][0] + m[2][0]*m[2][0]);
    }
    
    public float getHeight() {
        float[][] m = transf.asArray();
        return (float)Math.sqrt(m[0][1]*m[0][1] + m[1][1]*m[1][1] + m[2][1]*m[2][1]);
    }

    @Override
    protected void init() {}

    @Override
    public void update(long deltaMs) { }

    @Override
    protected void destroy() {}
}