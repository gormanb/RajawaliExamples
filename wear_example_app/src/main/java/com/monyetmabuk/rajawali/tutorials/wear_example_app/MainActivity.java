package com.monyetmabuk.rajawali.tutorials.wear_example_app;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.EllipticalOrbitAnimation3D;
import org.rajawali3d.animation.IAnimationListener;
import org.rajawali3d.lights.SpotLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.SpecularMethod;
import org.rajawali3d.math.MathUtil;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;
import org.rajawali3d.surface.IRajawaliSurfaceRenderer;
import org.rajawali3d.surface.RajawaliSurfaceView;
import org.rajawali3d.wear.RajawaliWearActivity;

public class MainActivity extends RajawaliWearActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initializeViewContent() {
        mRajawaliSurfaceView = new RajawaliSurfaceView(this);
    }

    @Override
    protected void onBeforeApplyRenderer() {
        
    }

    @Override
    public IRajawaliSurfaceRenderer createRenderer() {
        return new SpotLightRenderer(this);
    }

    @Override
    public int getLayoutID() {
        return 0;
    }

    private static final class SpotLightRenderer extends RajawaliRenderer {

        public SpotLightRenderer(Context context) {
            super(context);
            setFrameRate(60);
        }

        @Override
        protected void initScene() {
            final SpotLight spotLight = new SpotLight();
            spotLight.setPower(1.5f);

            getCurrentScene().addLight(spotLight);

            getCurrentCamera().setPosition(0, 2, 6);
            getCurrentCamera().setLookAt(0, 0, 0);

            Material sphereMaterial = new Material();
            sphereMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
            SpecularMethod.Phong phongMethod = new SpecularMethod.Phong();
            phongMethod.setShininess(180);
            sphereMaterial.setSpecularMethod(phongMethod);
            sphereMaterial.setAmbientIntensity(0, 0, 0);
            sphereMaterial.enableLighting(true);

            Sphere rootSphere = new Sphere(.2f, 12, 12);
            rootSphere.setMaterial(sphereMaterial);
            rootSphere.setRenderChildrenAsBatch(true);
            rootSphere.setVisible(false);
            getCurrentScene().addChild(rootSphere);

            // -- inner ring

            float radius = .8f;
            int count = 0;

            for (int i = 0; i < 360; i += 36) {
                double radians = MathUtil.degreesToRadians(i);
                int color = 0xfed14f;
                if (count % 3 == 0)
                    color = 0x10a962;
                else if (count % 3 == 1)
                    color = 0x4184fa;
                count++;

                Object3D sphere = rootSphere.clone(false);
                sphere.setPosition((float) Math.sin(radians) * radius, 0,
                    (float) Math.cos(radians) * radius);
                sphere.setMaterial(sphereMaterial);
                sphere.setColor(color);
                rootSphere.addChild(sphere);
            }

            // -- outer ring

            radius = 2.4f;
            count = 0;

            for (int i = 0; i < 360; i += 12) {
                double radians = MathUtil.degreesToRadians(i);
                int color = 0xfed14f;
                if (count % 3 == 0)
                    color = 0x10a962;
                else if (count % 3 == 1)
                    color = 0x4184fa;
                count++;

                Object3D sphere = rootSphere.clone(false);
                sphere.setPosition((float) Math.sin(radians) * radius, 0,
                    (float) Math.cos(radians) * radius);
                sphere.setMaterial(sphereMaterial);
                sphere.setColor(color);
                rootSphere.addChild(sphere);
            }

            final Object3D target = new Object3D();

            EllipticalOrbitAnimation3D anim = new EllipticalOrbitAnimation3D(
                new Vector3(0, .2f, 0), new Vector3(1, .2f, 1), 0, 359);
            anim.setRepeatMode(Animation.RepeatMode.INFINITE);
            anim.setDurationMilliseconds(6000);
            anim.setTransformable3D(target);
            anim.registerListener(new EmptyAnimationListener() {
                public void onAnimationUpdate(Animation animation, double interpolatedTime) {
                    spotLight.setLookAt(target.getPosition());
                }
            });
            getCurrentScene().registerAnimation(anim);
            anim.play();
        }

        @Override
        public void onOffsetsChanged(float v, float v1, float v2, float v3, int i, int i1) {

        }

        @Override
        public void onTouchEvent(MotionEvent motionEvent) {

        }
    }

    private static class EmptyAnimationListener implements IAnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationUpdate(Animation animation, double v) {
        }

    }

}
