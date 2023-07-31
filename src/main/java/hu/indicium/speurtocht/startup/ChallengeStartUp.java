package hu.indicium.speurtocht.startup;

import hu.indicium.speurtocht.domain.Challenge;
import hu.indicium.speurtocht.domain.ChallengeSubmission;
import hu.indicium.speurtocht.domain.Team;
import hu.indicium.speurtocht.service.ChallengeService;
import hu.indicium.speurtocht.service.TeamService;
import hu.indicium.speurtocht.utils.StartUpMultipartFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@Component
@Slf4j
@AllArgsConstructor
@Order(3)
@Transactional
public class ChallengeStartUp implements CommandLineRunner {

	private TeamService teamService;
	private ChallengeService challengeService;

	@Override
	public void run(String... args) throws Exception {
		Path path = Paths.get("./nature-1.jpg");
		String name = "nature-1.jpg";
		String originalFileName = "nature-1.jpg";
		String contentType = "image/jpg";
		byte[] content = null;
		try {
			content = Files.readAllBytes(path);
		} catch (final IOException e) {
			System.out.println(e);
		}
		MultipartFile result = new StartUpMultipartFile(name,
				originalFileName, contentType, content);

		Team team = teamService.getTeamByName("team-1");
		String test = """
				#1	High five!	Deel 15 gratis highfives uit aan onbekenden op straat.	5	
				#2	Ali Banaan	Maak een rap over bananen en geef de performance over een beat.	20	
				#3	Agent P (Knuffel I)	Knuffel een politieagent en bedank diegene voor hun bijdrage aan de maatschappij.	30	
				#4	Garbage Collector (Knuffel II)	Knuffel een vuilnisman en bedank diegene voor hun bijdrage aan de maatschappij.	20	
				#5	Checkpoint Charlie (Knuffel III)	Knuffel een parkeerwachter en bedank diegene voor hun bijdrage aan de maatschappij.	20	
				#6	Fred Teeven (Knuffel IV)	Knuffel een buschauffeur en bedank diegene voor hun bijdrage aan de maatschappij.	20	
				#7	Toetanchamon	Maak een menselijke piramide van minimaal 6 mensen.	30	
				#8	Doom	Speel Doom (1993) op je telefoon.	20	
				#9	Star-struck	Ontmoet een BN’er.	50	
				#10	Amstel Radler	Eet een hele citroen op.	10	
				#11	Jesus	Ga op de foto met een man met lang haar (bonuspunt als je het eerst mag vlechten).	10	
				#12	400m Neude	Ren met minimaal 5 groepsgenoten een race rond het grote terras op de Neude.	10	
				#13	Olie op het vuur	Spreek een random persoon aan en bedankt hem/haar voor die wilde nacht van afgelopen week.	10	
				#14	Katy Perry	Karaoke! Zing het couplet van ‘Last Friday Night’ van Katy Perry zo hard als je kan.	20	
				#15	Chemical Bonding	Lak een nagel van iedereen uit je groepje.	20	
				#16	Big Brother	Maak een foto met de vlag van een van de andere teams.	20	
				#17	Hashing	Verzamel 20 verschillende handtekeningen.	20	
				#18	Classy	Krijg een zoen van een vreemde met rode lippenstift (en laat het de hele dag zitten).	30	
				#19	TikTok	Doe met minimaal 3 groepsgenoten een tiktok-dansje onder de domtoren.	10	
				#20	Tom Scott	In een rood T-shirt, vertel iets merkswaardig startend met de zin: "I'm here at the..."	20	
				#21	Engine failure	Vouw een papierenvliegtuig en laat hem zo ver mogelijk vliegen.	5	
				#22	Gratis promotie	Plak de sticker van indicium op een zo hoog mogelijke plek	20	
				#23	git push origin main	Voer een goede daad uit waarbij je iemand helpt.	30	
				#24	Elevator pitch	Pitch een idee voor een website aan een vreemde op straat.	10	
				#25	Ameno	Maak een groepsfoto in een kerk terwijl je de kerkgangers respecteert.	10	
				#26	Stand-up comedian	Maak een vreemde aan het lachen.	20	
				#27	Broodje gezond	Ga naar een bakker en vraag waar de groente ligt.	30	
				#28	Inception	Maak een foto van iemand(1) die een foto maakt van iemand(2) die een foto maakt van iemand(3) die een foto maakt van iemand(4) die een foto maakt van iemand(5) die een foto maakt.	10	
				#29	Hoofd-Vergiet	Aanbid in de supermarkt het vliegend spaghettimonster door middel van een ritueel.	10	
				#30	Da Vinki?!	Voer chirurgie uit op een druif.	20	
				#31	De echte student	Tijd om te studeren! Lees aandachtig de eerste pagina van een boek in de bibliotheek.	30	
				#32	Old but gold	Maak een groepsfoto met een oud getrouwd koppel. (meer dan 30 jaar getrouwd)	10	
				#33	Utrechtenaar	Maak een groepsfoto met een pride vlag.	10	
				#34	Hideout vrijwiliger	Tap je eigen biertje.	30	
				#35	The floor is lava	Maak een groepsfoto waarop iedereen zijn voeten van de vloer heeft.	10	
				#36	Forever set in stone	Teken met krijt de naam van je groepje op de stoep.	20	
				#37	Ice Cube	Eet een ijsklontje op om op te frissen.	10	
				#38	Heilige Nijntje	Zegen een standbeeld van Nijntje.	10	
				#39	Menno I	Zonder behulp van papa's, mama's en de introcommissie achterhaal wie ‘Menno’ is en feliciteer hem.	40	
				#40	Eifeltoren	High five een teamgenoot met beide handen, zorg ervoor dat een vreemde onder deze boog loopt.	20	
				#41	Pirates of the oudegracht	Moedig kanoërs in de oude gracht aan.	10	
				#42	Snapchat My Bot	Wordt beste vrienden met chat-gpt.	10	
				#43	Tricking the System	Laat chat-gpt iets onethisch zeggen.	20	
				#44	Shame-GPT	Beken je meest beschamende vraag die je aan chat-gpt hebt gesteld.	5	
				#45	Greenpeace (Knuffel V)	Knuffel met je groepje een boom en bedank hem voor de zuurstof.	10	
				#46	Shakespear	Maak een liefdesgedicht en lees hem voor aan een vreemde.	20	
				#47	Tour d'Utrecht	Moedig fietsers aan bij een fietspad alsof het een bergetappe is van de Tour de France.	30	
				#48	Utrecht Marathon	Trek een sprintje onder de domtoren tegen een teamgenoot.	10	
				#49	Please stop it's already dead	Reconstrueer een dead meme.	5	
				#50	Forza Italia	Met een volle mond van een broodje mario, doe je beste mario en luigi impression.	10	
				#51	Buurman en Buurman	Perform een korte (15 á 30 seconden) zelfbedachte buurman en buurman sketch.	30	
				#52	Bootje Varen	Vouw een papieren bootje en laat hem varen in de oude gracht.	20	
				#53	Daily Standup	Doe een daily standup. (Wat heb je gister gedaan? Loop je nog tegen iets aan? Wat ga je vandaag doen?)	10	
				#54	Waterloo	Zing een refrein van een euro-songfestivalnummer.	20	
				#55	Salmonella	Eet een rauw ei.	30	
				#56	Tourguide	Geef een talk over een historisch monument aan je groepje alsof je een tourguide bent.	20	
				#57	CSS 101	Leg aan je papa’s of mama’s uit hoe je een div centreet.	20	
				#58	Nieuwsuur	Perform een serenade voor Mariëlle Tweebeeke van Nieuwsuur.	20	
				#59	Player	Maak een selfie waar bij je op beide wangen een kusje krijgt.	20	
				#60	Indicum	Spel het woord indicium met foto's van letters die je maakt in Utrecht	20	
				#61	Gabber	wees een oldschool gabber; gabber muziek aan en stampen maar.	5	
				#62	Climax	blaas een condoom op tot die knapt.	30	
				#63	Verzamelaar	fix 10 verschillende bierviltjes	10	
				#64	BIM 101	verkoop een rijstkorrel aan een onbekende	30	
				#65	Hideout	Ga met een barman op de foto	30	
				#66	IT-Specialist	Vraag een voorbijganger om uit te leggen wat een programmeertaal is, extra punten als ze het goed hebben.	30	
				#67	Brew install	Ga naar een café in Utrecht en probeer een bier te bestellen met een zelfbedachte technische term die niets te maken heeft met bier. 	20	
				#68	Smooth operator	Bedenk een ICT-gerelateerde pickup-line en probeer deze op een voorbijganger.	20	
				#69	Ijsbreker	Bel je crush op en beken de liefde aan diegene.	10	plus het geluk uit je toekomstige relatie
				#70	#Fristi	Drink frisdrank bij je ontbijt.	5	
				#71	Youth supremacy	Versla je Papa/Mama in een potje schaak.	10	
				#72	Ash Ketchum	Zing de Pokemon themesong zonder de tekst op te zoeken.	30	
				#73	Van jong naar oud	Maak een groepsfoto waar je hele groepje gesorteerd staat op leeftijd.	10	
				#74	Van klein naar groot	Maak een groepsfoto waar je hele groepje gesorteerd staat op lengte.	10	
				#75	Introcom <3	Maak een selfie met iedereen van de organisatie (blauw shirt).	20	
				#76	Mr. Robot	Kondig luidruchtig aan dat je De 'ICT-Superheld' bent en biedt mensen hulp aan met hun technologische problemen.	30	
				#77	Training Montage	Laat iemand 25 opdrukken.	25	
				#78	Guitar Hero	Bespeel een instrument (laat het professioneel lijken).	20	
				#79	Double-O Seven	Fluit de ‘mission impossible’ theme terwijl je een hapje eten van iemand steelt.	20	
				#80	Artificial Rain	Ga met je kleding aan onder de douche.	30	
				#81	Menno II	Wens je Papa / Mama een fijne verjaardag en zing met z’n allen een liedje voor ze.	20	
				#82	Nachtuil	leg het tijdstip 00:00 vast	10	
				#83	Passie Presentatie	Maak een powerpoint over iets waar je super gepassioneerd over bent en presenteer het aan je groepje.	30	
				#84	Joker	Doe mee aan een kaartspel	20	
				#85	GGPC	Vraag wanneer je eigen papa’s of mama’s tapdienst hebben om een ‘goud gele pret cilinder’	20	
				#86	Lov U Sponsoren	Bedank onze sponsoren dat ze op het kamp zijn gekomen.	10	
				#87	Heilige Peet	Kruip als Peet (een slang) 10m lang over de grond.	20	
				#88	Patatje Jenga	Bouw een Jenga toren van friet.	20	
				""";

		for (String s : test.split("\n")) {
			String[] split = s.split("	");
			Challenge challenge = this.challengeService.save(split[1], split[2], Integer.parseInt(split[3]));
			log.info("created challenge: #" + challenge.getId() + " " + challenge.getTitle());
			int randomInt = new Random().nextInt(88);
			if (randomInt >= 58) {
				ChallengeSubmission submission = this.challengeService.createSubmission(team, challenge, new MultipartFile[]{result, result, result});
				if (randomInt >= 68) {
					if (randomInt >= 78) {
						this.challengeService.approve(team, challenge.getId());
						log.info("approved submission for challenge: #" + challenge.getId() + " " + challenge.getTitle());
					} else {
						this.challengeService.deny(team, challenge.getId());
						log.info("denied submission for challenge: #" + challenge.getId() + " " + challenge.getTitle());
					}
				}
			}
		}
	}
}
