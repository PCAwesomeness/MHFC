package mhfc.net.client.model.mhfcmodel.animation;

import static org.lwjgl.opengl.GL11.glTranslatef;
import mhfc.net.client.model.mhfcmodel.Utils;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
/**
 * An animation to transform the model.
 *
 * @author WorldSEnder
 *
 */
public interface IAnimation {
	/**
	 * Describes a BoneTransformation. This includes rotation, translation and
	 * scaling.
	 *
	 * @author WorldSEnder
	 *
	 */
	public static class BoneTransformation {
		/**
		 * This interpolation mode will yield to following results:<br>
		 * <code>factor < 1.0F</code>: the left transformation<br>
		 * <code>factor >= 1.0F</code>: the right transformation
		 */
		public static final int CONSTANT = 0;
		/**
		 * For every value the 'left' and 'right' {@link BoneTransformation}s
		 * argument hold, the value in the result will be<br>
		 * <code>(1-factor)*left + factor*right</code>.
		 */
		public static final int LINEAR = 1;
		/**
		 * For every value the 'left' and 'right' {@link BoneTransformation}s
		 * argument hold, the value in the result will be<br>
		 * <code>(1-factor)^2*(2*factor+1)*left + factor^2*(3-2*factor)*right</code>
		 * .<br>
		 * Note: this holds that no two different factors will generate the same
		 * output value (if left and right differ and factor is in [0, 1])
		 */
		public static final int SPLINE = 3;

		public static final BoneTransformation identity = new BoneTransformation();

		private Quaternion rotationQuat;
		private Vector3f translation;
		private float scale;

		public BoneTransformation() {
			this(new Quaternion(), new Vector3f(), 1.0F);
		}

		public BoneTransformation(Quaternion quat, Vector3f translation,
				float scale) {
			glTranslatef(scale, scale, scale);
			this.rotationQuat = quat;
			this.translation = translation;
			this.scale = scale;
		}

		public Matrix4f asMatrix() {
			return Utils.fromRotTrans(this.rotationQuat, this.translation,
					scale);
		}
		/**
		 * Interpolates between two {@link BoneTransformation}s and returns
		 * their interpolation. This is can be useful when using the current
		 * subFrame as the factor.
		 *
		 * @param left
		 *            the "left" transformation of two to interpolate between
		 * @param right
		 *            the "right" transformation of two to interpolate between
		 * @param factor
		 *            how much the left/right Transformation should influence
		 *            the outcome. A value of 0 always means that the left
		 *            transform is returned, a value of 1 will always output the
		 *            right transformation
		 * @param mode
		 *            the mode to use. Use the symbolic constants
		 * @return
		 */
		public static BoneTransformation interpolate(BoneTransformation left,
				BoneTransformation right, float factor, int mode) {
			// TODO: interpolation algorithms
			return null;
		}
	}
	/**
	 * Returns the bone's current {@link BoneTransformation} (identified by
	 * name). If is returned by this method it is assumed that Bone is in his
	 * binding-pose-state (identity transform).<br>
	 * The returned matrix should already be interpolated correctly.
	 *
	 * @param bone
	 *            the name of the bone the matrix is requested
	 * @param frame
	 *            the current frame in the animation
	 * @param subFrame
	 *            the subFrame in the animation. Expect this to be in [0,1]
	 */
	public BoneTransformation getCurrentTransformation(String bone, int frame,
			float subFrame);
}