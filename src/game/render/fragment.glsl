#version 400 core

in vec2 texmap_;
in vec3 position_;
in vec3 normal_;

out vec4 pixel;

uniform sampler2D sampler;
uniform bool textured;
uniform vec4 colour;

uniform vec3 lightPos[20];
uniform vec4 lightColour[20];
uniform float lightExpAtt[20];
uniform float lightLinAtt[20];
uniform int numLights;
uniform vec3 camPos;
uniform bool lightingOn;

void main(void) {

	pixel = vec4(0.3, 0.3, 0.3, 1.0);
	
	vec4 realColour = colour/255.0;
	if(textured) {
		realColour *= texture(sampler, texmap_);
	}
	
	if(lightingOn) {
		
		for(int i = 0; i < numLights; i++) {
		
			vec3 lightDir = normalize(lightPos[i] - position_);
			vec3 camDir = normalize(camPos - position_);
			
			float lightDist = length(lightPos[i] - position_);
			float att = pow(1-lightExpAtt[i], lightDist*lightLinAtt[i]);
			
			vec4 lColour = lightColour[i]/255.0 * att;
			pixel += lColour * dot(lightDir, normalize(normal_)) * realColour;
			
			float spec = dot(lightDir, reflect(camDir, normalize(normal_)));
			spec = clamp(spec, 0.0, 1.0);
			spec = 0.6*pow(spec, 15.0);
			
			pixel += lColour*spec;
		}
	} else {
		pixel = realColour;
	}
	pixel.w = 1.0;
}