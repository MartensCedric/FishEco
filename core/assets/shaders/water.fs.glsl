#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_refraction;
uniform float timef;
uniform float world_x;
uniform float world_y;
uniform float screen_width;
uniform float screen_height;

void main()
{
	float timeMod = timef * 0.125;
	vec2 scrolledCoords = v_texCoords + vec2(timeMod);

	scrolledCoords.x = fract(scrolledCoords.x);
	scrolledCoords.y = fract(scrolledCoords.y);

    float distortionStrength = 0.05;

	vec4 ref = texture2D(u_refraction, scrolledCoords);
	vec2 displacement = fract(v_texCoords + (ref.bb * distortionStrength));

    vec4 color = texture2D(u_texture, displacement);

    if(color.w == 0.0)
        discard;

	gl_FragColor = color;
}