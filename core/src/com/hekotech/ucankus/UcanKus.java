package com.hekotech.ucankus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class UcanKus extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture bird;
	int puan;
	float bx, by, bw, bh, arix, ariy;
	float sh, sw;
	float gravity = 0.3f; //yer çekimi
	float kusHiz = 0.0f; //kuşun düşme hızı
	int state = 0;
	Texture ari1, ari2, ari3;
	int arisayisi = 3;
	float arilarX[] = new float[arisayisi];
	float mesafe;
	float arilarY[][] = new float[3][arisayisi];
	Circle c_kus;
	Circle c_ari1[] = new Circle[arisayisi];
	Circle c_ari2[] = new Circle[arisayisi];
	Circle c_ari3[] = new Circle[arisayisi];

	boolean flag = true;
	BitmapFont skorFont;
	BitmapFont font;
	Sound sound;

	ShapeRenderer sr;//oluşturduğumuz hitboxı görmemize yarar.

	@Override
	public void create () {

		batch = new SpriteBatch(); //tuval
		img = new Texture("background1.png");
		bird = new Texture("bird1.png");
		ari1 = new Texture("bee1.png");
		ari2 = new Texture("bee2.png");
		ari3 = new Texture("bee1.png");

		sw = Gdx.graphics.getWidth();
		sh = Gdx.graphics.getHeight();
		bx = sw /4;//kuşun x eksenindeki konumu
		by = sh /3;//kuşun y eksenindeki konumu
		bw = sw /11; //kuşun genişliği
		bh = sh /8; //kuşun yüksekliği
		arix = 1200;
		ariy = 300;
		mesafe = Gdx.graphics.getWidth()/2; //iki arı arası mesafe

		skorFont = new BitmapFont();
		skorFont.setColor(Color.GREEN);
		skorFont.getData().setScale(10);

		sound = Gdx.audio.newSound(Gdx.files.internal("lose.ogg"));

		font = new BitmapFont();
		font.setColor(Color.BLACK);
		font.getData().setScale(14);


		sr = new ShapeRenderer();//hitbox görmek

		c_kus = new Circle();
		c_ari1 = new Circle[arisayisi];
		c_ari2 = new Circle[arisayisi];
		c_ari3 = new Circle[arisayisi];


		for(int i=0; i<arisayisi; i++){//dizi

			arilarX[i] = Gdx.graphics.getWidth() + i*mesafe;
			Random r1 =new Random();
			Random r2 =new Random();
			Random r3 =new Random();

			arilarY[0][i] = r1.nextFloat() * Gdx.graphics.getHeight();
			arilarY[1][i] = r1.nextFloat() * Gdx.graphics.getHeight();
			arilarY[2][i] = r1.nextFloat() * Gdx.graphics.getHeight();
			c_ari1[i] = new Circle();
			c_ari2[i] = new Circle();
			c_ari3[i] = new Circle();

		}

	}

	@Override
	public void render () {

		batch.begin(); //arka planı başlat
		//ekranda gözükecek her şey
		batch.draw(img, 0, 0, sw, sh); //görseli çiz ve konumlandır

		if(state == 1){ //ekrana bir kere dokunuldu mu?

			if(Gdx.input.justTouched()){ //zıplama hızı
				kusHiz = -7;

			}
			for(int i=0; i<arisayisi; i++){

				if(arilarX[i]<bw){
					arilarX[i] =+ arisayisi*mesafe;

					Random r1 =new Random();
					Random r2 =new Random();
					Random r3 =new Random();

					arilarY[0][i] = r1.nextFloat() * Gdx.graphics.getHeight()-bh;
					arilarY[1][i] = r2.nextFloat() * Gdx.graphics.getHeight()-bh;
					arilarY[2][i] = r3.nextFloat() * Gdx.graphics.getHeight()-bh;
				}
				//skor belirleme
				if(bx>arilarX[i] && flag){

					puan++;
					System.out.println(puan);
					flag = false;
				}
				if(arilarX[i]<bw+4){

					flag = true;
				}
				arilarX[i] -= 4;
				batch.draw(ari1, arilarX[i], arilarY[0][i], bw, bh);
				batch.draw(ari2, arilarX[i], arilarY[1][i], bw, bh);
				batch.draw(ari3, arilarX[i], arilarY[2][i], bw, bh);
			}

			if(by<0){

				state = 2;
				by = sh/3;
				kusHiz = 0; //yere düştükten sonra kuşun hızı sıfırlansın ki baştan başlasın
			}
			else{
				//tam olarak yer çekimi hissi oluşturur
				kusHiz = kusHiz + gravity; //düşerken kuşun hızını arttırır.
				by = by - kusHiz; //kuşun dikey konumunu azaltır
			}
		}
		else{
			if(Gdx.input.justTouched()){

				state = 1;
			}
		}
		batch.draw(bird, bx , by, bw, bh);

		//skoru yazdırma
		skorFont.draw(batch,String.valueOf(puan), Gdx.graphics.getWidth()-bw,bh);

		c_kus.set(bx+bw/2,by+bh/2,bw/2);

		/*sr.begin(ShapeRenderer.ShapeType.Filled); //hitbox görmek
		sr.setColor(Color.BLUE);
		sr.circle(bx+bw/2,by+bh/2,bw/2);*/

		for(int i=0; i<arisayisi; i++){

			//ariların hitboxunun boyutunu ve konumunu ayarlar
			/*sr.circle(arilarX[i]+bw/2,arilarY[0][i]+bh/2,bw/2);
			sr.circle(arilarX[i]+bw/2,arilarY[1][i]+bh/2,bw/2);
			sr.circle(arilarX[i]+bw/2,arilarY[2][i]+bh/2,bw/2);*/

			c_ari1[i].set(arilarX[i]+bw/2,arilarY[0][i]+bh/2,bw/2);
			c_ari1[i].set(arilarX[i]+bw/2,arilarY[1][i]+bh/2,bw/2);
			c_ari1[i].set(arilarX[i]+bw/2,arilarY[2][i]+bh/2,bw/2);

			//kuşun ve arıların çarpışması durumunda neler olucak
			if(Intersector.overlaps(c_kus,c_ari1[i]) || Intersector.overlaps(c_kus,c_ari2[i]) || Intersector.overlaps(c_kus,c_ari3[i])){

				font.draw(batch, "Oyun Bitti! Tekrar Denemek İçin Dokun",
						Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
				sound.play();
				sound.dispose();

				state = 2;
				by = sh/3;
				kusHiz = 0;
			}

		}
		batch.end();

		sr.end();

	}
	
	@Override
	public void dispose () {


	}
}
