package mhfc.net.client.model.mhfcmodel.glcontext;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL41.GL_FRAGMENT_SHADER_BIT;
import static org.lwjgl.opengl.GL41.GL_PROGRAM_PIPELINE_BINDING;
import static org.lwjgl.opengl.GL41.GL_PROGRAM_SEPARABLE;
import static org.lwjgl.opengl.GL41.GL_TESS_CONTROL_SHADER_BIT;
import static org.lwjgl.opengl.GL41.GL_TESS_EVALUATION_SHADER_BIT;
import static org.lwjgl.opengl.GL41.GL_VERTEX_SHADER_BIT;
import static org.lwjgl.opengl.GL41.glBindProgramPipeline;
import static org.lwjgl.opengl.GL41.glGenProgramPipelines;
import static org.lwjgl.opengl.GL41.glProgramParameteri;
import static org.lwjgl.opengl.GL41.glUseProgramStages;
import mhfc.net.client.model.mhfcmodel.IRenderInformation;
import mhfc.net.client.model.mhfcmodel.Utils;
import mhfc.net.client.model.mhfcmodel.animation.IAnimatedObject;
import mhfc.net.client.model.mhfcmodel.animation.IAnimation;
import mhfc.net.client.model.mhfcmodel.data.ModelData40;
import mhfc.net.client.model.mhfcmodel.data.RawDataV1;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Predicate;

public class GLHelper40 extends GLHelper {
	protected static final int EXTERNAL_SHADER_BITS = GL_FRAGMENT_SHADER_BIT
			| GL_TESS_CONTROL_SHADER_BIT | GL_TESS_EVALUATION_SHADER_BIT;

	private static volatile boolean initiated = false;
	private static int programName;
	private static int pipelineName;
	private static int shaderName = 0;

	private static void init() {
		try {
			if (shaderName == 0) // Not already done on previous tries
				shaderName = Utils.compileShaderSafe(GL_VERTEX_SHADER,
						GLHelper40.class.getResourceAsStream("default40.vsh"));
			if (programName == 0)
				programName = Utils.createProgramSafe();
			glAttachShader(programName, shaderName);
			glProgramParameteri(programName, GL_PROGRAM_SEPARABLE, GL_TRUE);
			Utils.linkProgramSafe(programName);
			glDeleteShader(shaderName);
			pipelineName = glGenProgramPipelines();
			glUseProgramStages(pipelineName, GL_VERTEX_SHADER_BIT, programName);
		} catch (Exception e) {
			throw new IllegalStateException(
					"Error setting up OpenGL enviroment.", e);
		}
		initiated = true;
	}

	private static void ensureInit() {
		if (!initiated)
			init();
	}

	private ModelData40 modelData;

	@Override
	public void loadFrom(RawDataV1 datav1) {
		if (this.modelData != null) {
			// Thread safe, you never know when you need it
			ModelData40 oldData = this.modelData;
			this.modelData = new ModelData40(datav1);
			oldData.free();
		} else {
			this.modelData = new ModelData40(datav1);
		}
	}

	@Override
	public void render(IAnimatedObject animatedObject, float subFrame) {
		// FIXME: this doesn't render, maybe it's the shader?? Try with compute
		// shader
		if (this.modelData == null) // An invalid model, etc...
			return;
		GL11.glPushMatrix();
		glEnableClientState(GL_VERTEX_ARRAY);
		ensureInit();
		int currProgram = glGetInteger(GL_CURRENT_PROGRAM);
		int currPipeline = glGetInteger(GL_PROGRAM_PIPELINE_BINDING);
		// TODO: adapt fragment shader,etc. into pipeline if possible
		// glUseProgramStages(pipelineName, EXTERNAL_SHADER_BITS, currProgram);
		glBindProgramPipeline(pipelineName);
		glUseProgram(0);

		IRenderInformation info = animatedObject.getRenderInformation();
		IAnimation currentAttack = info.getCurrentAnimation();
		int frame = info.getCurrentFrame();
		Predicate<String> filter = info.getPartFilter(subFrame);
		this.modelData.renderFiltered(filter, currentAttack, frame, subFrame);

		glUseProgram(currProgram);
		glBindProgramPipeline(currPipeline);
		glDisableClientState(GL_VERTEX_ARRAY);
		GL11.glPopMatrix();
	}
}