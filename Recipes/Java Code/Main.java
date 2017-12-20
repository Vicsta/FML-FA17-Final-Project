import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
	
	private final static String USER_AGENT = "Mozilla/5.0";
	private final static String FILE_PATH = "C:/Users/Victor/Desktop";
	
	private static ArrayList<String> keys = new ArrayList<String>();
	
	private static ArrayList<String []> cats = new ArrayList<String []>();
	private static ArrayList<Feature> categories = new ArrayList<Feature>();
	
	private static ArrayList<String> measures = new ArrayList<String>();
	
	private static ArrayList<String> vol = new ArrayList<String>();
	
	private static HashMap<String, Double> density = new HashMap<String, Double>();
	private static HashMap<String, Double> weight = new HashMap<String, Double>();
	private static HashMap<String, Double> conversions = new HashMap<String, Double>();
	
	private static HashMap<String, Double> miscConv = new HashMap<String, Double>();
	
	private static Double[] features;
	
	private static int notDone = 0;
	private static int notConv = 0;
	
	private static int unuseable = 0;
	
	private static int maxCat = 0;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		//String[] items = {"g\\u00eanoise sheet", "gold leaf", "baking sheet", "serving platter", "(make", "star cutter", "pie plate", "cedar plank", "paintbrush", "masking tape", "metal rack", "waffle cup",  "easy aioli", "oven mitts", "chopsticks", "grinder", "cut into 6 wedges each:", "ingredients for variations (see below)", "plastic wrap", "the coating (see above)", "kaolin", "dye", "dowel", "peeler", "plastic bag", "store extra", "cardboard round", "vitamin c", "preheat", "1 cup packed, washed and spun", "chive blossoms", "cardoboard", "borage", "organic rose", "cast-iron", "sprinkles", "pizza stone", "smoker box", "glassware", "none is needed", "pine twigs", "rose petals", "fireplace", "flower", "mixer", "cork", "towel", "jar", "2 servings", "can be prep", "seltzer", "pot", "measuring", "sifter", "cooker", "whisk",  "wood chips", "for marinade", "knife", "casserole", "strainer", "mixing", "bowl", "spit", "blender", "edible rose", "roasted vegetable rag\\u00f9", "garni", "spatula", "lid", "containers with lids", "wooden dowel", "white roses and fres", "ounce flask", "halved through root", "ounce tube", "speed blender", "drag\\u00e9es", "n/a", "ramps", "wrapper", "storage", "recipe", "blade", "needle",  "styrofoam", "gaga filling", "kettle", "spoon","paper", "seafood glaze", "plastic cup", "drinking glass", "straws", "N/A", "skewer", "edible flowers", "makes about", "available", "[]", "pan", "skillet", "baking dish", "mold", "coloring", "ramekin", "parchment", "*available", "**available", "picks", "butcher shop", "thermometer"};
		
		//VEGETABLES
		String[] mildLeafy = {"purslane", "cabbage", "baby greens", "mixed greens", "fris", "slaw", "escarole", "romaine", "bibb lettuce", "lettuce", "microgreens", "sprouts", "mesclun", "alfalfa", "salad"};
		String[] strongLeafy = {"spinach", "artichoke", "endive", "arugula", "tatsoi", "m\\u00e2che", "mizuna", "rocket"};
		String[] medChilis = {"peperoncini", "jalape", "chilies", "red chilli", "fresh red chili", "ancho chili", "dried chili", "espelette", "anaheim chili", "pasilla", "mexico chili", "poblano chili", "green chili", "rajas", "chillies"};
		String[] killMouthChilis = {"yellow chili", "serrano chili", "serranos", "aji amarillo", "mama lil's kick butt", "habanero chili", "bird chili", "chili pod", "chili pods", "haba\\u00f1ero"};
		String[] onion = {"shallot", "onion", "scallion", "leek", "chive pieces", "chives"};
		String[] tomatos = {"tomato", "tomatillo", "tomate verde"};
		String[] bellPepper = {"red bell"};
		String[] cucumber = {"zucchini", "cucumber", "cactus"};
		String[] pickled = {"kuchela", "caper", "cornichon", "relish", "sauerkraut", "gherkin", "pickles", "kimchi"};
		String[] greenStalkLike = {"asparagus", "broccoli", "yau choy", "fiddlehead fern stems"};
		String[] greenProminent = {"fennel", "celery", "hearts of palm", "rhubarb", "bok choy"};
		String[] soySub = {"soy chunk", "edamame", "tofu", "meatless patties", "soy tempeh"};
		String[] beans = {"lima bean", "kidney beans", "bean", "frijoles", "haricot"};
		String[] freshSalad = {"pico de gallo", "tabbouleh", "crudit\\u00e9s"};
		String[] rootVeg = {"jicama", "radish", "radicchio", "parsnip", "rutabaga", "celeriac", "kohlrabi", "burdock", "j\\u00edcama", "white yaut", "daikon", "turnip", "root veg", "beet"};
		String[] shrooms = {"mushroom", "shiitake", "chanterelle", "porcini"};
		String[] seaGreens = {"nori", "aziki", "rockweed", "kelp", "kombu", "seaweed"};
		String[] potatoes = {"batata", "yam", "\\u00f1ame", "potatoes", "potato"};
		String[] exotics = {"bamboo", "truffle", "morel", "okra"};
		String[] squashesLike = {"squash", "pumpkin", "xuxu", "chayote", "yuca"};
		String[] sideDishes = {"hominy", "carrot", "avocado", "corn", "cauliflower", "olives", "olive", "peas", "pea", "vegetable", "greens"};

		//GRAINS
		String[] flatbread = {"pita", "matzo", "matzoh", "pappadam", "tortilla", "tostada", "wraps", "taco shell", "matzah", "papadum", "lavash"};
		String[] crunchy = {"germ", "cereal", "granola"};
		String[] pasta = {"lasagne", "fettuccine", "fusilli", "bun", "gnocchi", "penne", "linguine", "macaroni", "rigatoni", "ziti", "gnocchetti", "fideo", "farfalle", "capellini", "orecchiette", "pappardelle", "maccheroncini", "lasagna", "chitarra", "acini di pepe", "gemelli", "fettucine", "rotelle", "trenett", "bucatini", "tagliatelle", "spaghetti", "pasta"};
		String[] stuffedPasta = {"cappelletti", "herb dumpling", "tortellini"};
		String[] flour = {"masa harina", "semolina", "seita", "tvp", "all-purpose f lour", "sorghum", "cracked wheat", "flour"};
		String[] bread = {"dough", "bread baguette", "white bread", "pita bread", "breadcrumbs", "grain roll", "bread roll",  "bread crumbs", "baguette", "pizza crust", "bread", "crackers", "loaf", "toast", "dinner roll", "beurre mani", "roll", "crouton", "focaccia", "crust", "pretzel", "spelt", "biscotti", "croissant", "rye", "pizza shell", "ciabatta"};
		String[] cornType = {"grits", "polenta", "ornmeal"};
		String[] soupType = {"noodle", "won ton", "wonton", "somen"};
		String[] dessertType = {"cannoli shell", "pie crust", "phyllo","puff pastry", "puff pasty", "muffin", "pastry", "crepes", "biscuit", "brioche", "tart shell", "pie shell", "bisquick", "cr\\u00eapes", "cr\\u00eape"};
		String[] potatoSides = {"potato chips", "hash brown", "tater tot", "fries"};
		String[] seedType = {"farro", "barley", "quinoa", "rice", "lentil", "seeds", "oats", "bran", "flaxseed", "orzo", "couscous", "bulgur", "millet", "oatmeal", "freekeh", "tehina", "kasha", "amaranth"};

		//DAIRY
		String[] fat = {"olive oil", "oil", "sesame oil", "spray", "lard", "schmaltz", "fat", "suet", "cr\\u00e8me fra\\u00eeche", "shortening", "shorting", "earth balance", "ghee", "margarine", "butter"};
		String[] cream = {"milk", "sour cream", "heavy cream", "whipping cream", "lactose", "cr\\u00e9me fra\\u00eeche", "cr\\nme fra\\u00eeche", "half and half", "cream", "half-and-half"};
		String[] egg = {"egg", "yolk"};
		String[] yogurt = {"yoghurt", "yogurt"};

		//CHEESE
		String[] weakCheese = {"havarti", "burrata", "queso", "quark", "ricotta", "gruy", "mascarpone", "cheese", "mozzarella"};
		String[] mildCheese = {"pecorino", "gouda", "parmigiano", "asiago", "emmenthal", "parmeasan", "grana padano", "fromage", "shredded gouda", "neufcha", "monterey jack", "provolone", "fontina", "romano", "parmesan" , "cream cheese", "cheddar", "sharp cheddar"};
		String[] strongCheese = {"haloumi", "brie", "m\\u00fcnster", "m\\u00dcnster", "roquefort", "camembert", "gorgonzola", "swiss", "feta", "goat cheese"};
		
		//FRUIT
		String[] fruit = {"cantaloupe", "guanabana", "mandarin", "mixed berrie", "pluots", "assorted fruit", "dried fruit", "persimmon", "clementine", "canteloupe", "melon", "tropical fruits", "plantain", "berries", "lychees", "kiwi", "raspberr", "mango", "grape", "cherries", "coconut", "banana", "tangerine", "apricot", "strawberr", "date",  "blueberr", "nectarine", "pear", "raisin", "fig", "kumquat", "apple", "currant", "orange", "fruit", "fruits"};
		String[] sourFruit = {"fresh guava", "umeboshi", "passion fruit", "line", "papaya", "nasturtium", "quince", "cherry", "passion-fruit", "prune", "blackberr", "grapefruit", "plum", "pomegranate", "sumac", "tamarind", "cranberry", "cranberries", "cherries",  "lime", "lemon"};

		//HERBS
		String[] basil = {"basil", "oregano", "thyme", "bay leaves", "bay leaf", "tarragon", "sorrel", "summer savory", "fresh savory", "or\\u00e9gano", "leaves", "fresh herb", "herbes", "salad herbs"};
		String[] parsley = {"parsley","dill", "rosemary", "sage", "coriander", "chervil", "gremolata"};
		String[] mint = {"mint"};
		String[] cilantro = {"cilantro"};
		String[] fragrant = {"marjoram", "saffron", "turmeric", "clove", "lavender", "vanilla", "coffee", "galangal", "tea bag"};

		//SAUCES
		String[] fluids = {"chicken broth", "vegetable stock", "chicken stock", "water", "veal stock", "ice cube", "club soda", "clam juice", "vegetable broth", "dashi", "sea stock", "tea", "ice", "fish stock", "stock", "epazote", "crushed ice", "bouillon", "bouillon base"};
		String[] basic = {"pesto sauce", "vinegar", "gelatin", "salsa", "enchilada sauce", "hummus", "poppy seed dressing", "gravy", "pesto", "crema", "chutney", "dressing", "mutabal", "rag\\u00f9 alla bolognese", "marinara", "guacamole"};
		String[] sweet = {"cider", "preserves", "barbecue sauce", "teriyaki marinade", "chili fruit sauce", "teriyaki baste", "sweet and sour mix"};
		String[] savory = {"curry", "stir-fry sauce", "vinaigrette", "soy sauce", "fish sauce", "duck sauce", "hoisin sauce", "sesame paste", "sherry", "tapenade", "worcestershire", "oyster sauce", "chimichurri", "adobo sauce", "rouille", "picada", "sofrito", "mojo verde", "bbq marinade", "salsa", "salasa", "a\\u00efoli", "soy glaze", "sauce"};
		String[] spicy = {"curry paste", "tabasco", "chili sauce", "hot sauce", "chili paste", "sriracha", "harissa", "tobasco", "cholula", "sambal oelek"};
		String[] condiment = {"mustard", "tahini", "condiments", "catsup", "vegenaise", "mayonaise", "mayo", "mayonnaise"};
		String[] tangy = {"raita refresh", "lemon juice", "red mole", "espresso", "ponzu", "vincotto", "tamari", "ancho mole", "mojo criollo", "mole poblano"};

		//SPICES
		String[] starch = {"arrowroot", "starch", "tapioca"};
		String[] baking = {"yeast", "baking soda", "baking powder"};
		String[] hardSpicy = {"cumin", "cayenne", "paprika", "chili powder", "curry powder", "chile", "wasabi", "chili flake", "zhoug", "fil\\u00e9 powder", "ancho powder"};
		String[] savorySpice = {"salt", "black pepper", "garlic", "pepper", "chipotle", "miso", "five-spice", "seasoning", "baharat", "marrow", "stuffing", "rub #", "fleur de sel", "za'atar", "asafetida", "kona-zansh\\u00f5", "t\\u00f5garashi", "barbecue rub", "ajvar", "sapporo", "dry rub", "peperonata", "sel de guer"};
		String[] sweetSpice = {"sugar", "pimiento"};
		String[] nuttySpice = {"mahleb", "cocoa powder", "cocoa", "cacao"};
		String[] tangySpice = {"nutmeg", "cloves", "ginger", "tumeric", "cinnamon", "allspice", "aniseed", "anise", "cardamom", "menthe", "masala", "canela"};

		//NUTS
		String[] nuts = {"brazil nut", "ground nut", "raw nut", "chopped nuts", "mixed nuts", "cashew", "pistachio", "macadamia", "pecan", "pine nut", "walnut", "hazelnut", "almond", "chestnut"};

		//SWEETS
		String[] cookie = {"de cacao", "cookie", "chocolate", "maraschino cherry", "macaroon",  "marshmallows", "cake", "ladyfingers", "marshmallow", "candied fruit", "candy cane", "candie", "marrons", "toffee", "candy", "gum", "m&m", "meringue", "choclo kernel", "oreo"};
		String[] syrup = {"honey", "maple syrup", "sprite", "caramel", "sweet tea", "nutella", "dulce de leche", " fudge sauce", "pectin", "cola", "topping", "jam", "punch", "marmalade", "sorbet", "soda", "cajeta", "icing", "maple", "cool whip", "frosting", "syrup"};
		String[] sweetenerNat = {"agave nectar", "molasses", "piloncillo", "guave nectar", "stevia", "guava nectar", "sucanat"};
		String[] sweetenerArt = {"splenda"};
		
		//MEATS
		String[] smallAnimal = {"chicken", "turkey", "duck", "hen", "quail", "squab", "capon", "goose", "rabbit", "pheasant"};
		String[] mediumAnimal = {"pork", "bacon", "ham", "lamb", "pancetta",  "prosciutto", "veal", "kishke","salami", "giblets", "liver", "wieners", "kielbasa", "pig", "mincemeat", "chopmeat", "shank", "bones", "snail", "terrine", "p\\u00e2te sucr\\u00e9e disk", "nduja", "chorizo", "bratwurst", "knockwurst", "hot dog", "p\\u00e2te bris\\u00e9e", "boar meat", "pastrami"};
		String[] bigAnimal = {"steak", "beef", "chuck", "oxtail", "ribs", "rib", "sirloin", "mignon", "buffalo", "brisket", "bison", "ground round", "stew meat", "venison"};

		//SEAFOOD
		String[] whiteFish = {"fleshed-fish", "skate wing", "mahi-mahi", "anchovy", "flounder", "carp", "cod", "tilapia", "halibut", "bluefish", "mackerel", "bass", "fleshed fish", "sole", "anchovies", "sardines", "catfish", "rouget", "fish bone", "finnan", "herring", "mahimahi", "mahi mahi", "trout",  "white fish", "whitefish", "monkfish", "fish"};
		String[] redFish = {"snapper", "salmon", "bonito", "char", "swordfish", "tuna", "filet", "fillet"};
		String[] shellfish = {"shrimp", "crab", "lobster", "crabmeat", "prawns"};
		String[] seafood = {"conch", "scallops", "mussels", "clam", "oysters", "caviar", "roe", "octopus", "squid", "calamari", "periwinkle", "scallop"};

		//ALCOHOL
		String[] alcohol = {"cassis", "t\\u00eca maria", "galliano", "cacha\\u00e7a", "grand mariner", "tuaca", "moselle", "spirit", "kahl\\u00faa", "framboise", "muscadet", "cura\\u2027ao", "stout", "burgundy", "ros\\u00e9", "mezcal", "absinthe", "sauvignon", "bordeaux", "aquavit", "midori", "orvieto", "dubonnet", "cava", "lager", "pisco", "grater", "claret", "sambuca", "aperol", "pinot", "cura\\u00e7ao", "amaro", "cr\\u00e8me", "riesling", "chimay", "pimm's", "campari", "guinness", "cabernet", "scotch", "limoncello", "syrah", "armagnac", "maraschino", "cointreau", "amaretto", "absente", "dictine", "bitters", "sauternes", "southern comfort", "verjus", "grappa", "cognac", "grand marnier", "frangelico", "marsala", "zinfandel", "bourbon", "sake", "mirin", "calvados", "merlot", "k\\u00fcmmel", "kirsh", "kirsch", "tequila", "chartreuse", "madeira", "sec", "port", "beer", "ale", "whisky", "drambuie", "lillet", "ouzo", "angostura bitters", "pernod", "vermouth", "grenadine", "rum", "liqueur", "gin", "vodka", "ketchup", "dry red wine", "brandy", "whiskey", "tawny port", "dry white", "wine"};
		
		cats.add(mildLeafy);
		cats.add(strongLeafy);
		cats.add(medChilis);
		cats.add(killMouthChilis);
		cats.add(onion);
		cats.add(tomatos);
		cats.add(bellPepper);
		cats.add(cucumber);
		cats.add(pickled);
		cats.add(greenStalkLike);
		cats.add(greenProminent);
		cats.add(soySub);
		cats.add(beans);
		cats.add(freshSalad);
		cats.add(rootVeg);
		cats.add(shrooms);
		cats.add(seaGreens);
		cats.add(potatoes);
		cats.add(exotics);
		cats.add(squashesLike);
		cats.add(sideDishes);
		
		cats.add(flatbread);
		cats.add(crunchy);
		cats.add(pasta);
		cats.add(stuffedPasta);
		cats.add(flour);
		cats.add(bread);
		cats.add(cornType);
		cats.add(soupType);
		cats.add(dessertType);
		cats.add(potatoSides);
		cats.add(seedType);
		
		cats.add(fat);
		cats.add(cream);
		cats.add(egg);
		cats.add(yogurt);
		
		cats.add(weakCheese);
		cats.add(mildCheese);
		cats.add(strongCheese);
		
		cats.add(fruit);
		cats.add(sourFruit);
		
		cats.add(basil);
		cats.add(parsley);
		cats.add(mint);
		cats.add(cilantro);
		cats.add(fragrant);
		
		cats.add(fluids);
		cats.add(basic);
		cats.add(sweet);
		cats.add(savory);
		cats.add(spicy);
		cats.add(condiment);
		cats.add(tangy);
		
		cats.add(starch);
		cats.add(baking);
		cats.add(hardSpicy);
		cats.add(savorySpice);
		cats.add(sweetSpice);
		cats.add(nuttySpice);
		cats.add(tangySpice);
		
		cats.add(nuts);
		
		cats.add(cookie);
		cats.add(syrup);
		cats.add(sweetenerNat);
		cats.add(sweetenerArt);
		
		cats.add(smallAnimal);
		cats.add(mediumAnimal);
		cats.add(bigAnimal);
		
		cats.add(whiteFish);
		cats.add(redFish);
		cats.add(shellfish);
		cats.add(seafood);
		
		cats.add(alcohol);
		
		//cats.add(items);
		
		for(int c = 0; c < cats.size(); c++) {
			for(int i = 0; i < cats.get(c).length; i ++) {
				Feature f = new Feature(cats.get(c)[i], maxCat, maxCat);
				categories.add(f);
			}
			if(c != cats.size() - 1) {
				maxCat++;
			}
		}
		maxCat++;
		
		/*
		File file = new File("ingredients_sparse.txt");
		Scanner scan = new Scanner(file);
		
		int c = 0;
		while(scan.hasNextLine()) {
			String text = scan.nextLine();
			String[] inC = text.split(",");
			for(int i = 0; i < inC.length; i++) {
				int s = -1;
				for(int x = 0; x < cats.size(); x++) {
					for(int z = 0; z < cats.get(x).length; z++) {
						if(inC[i].contains(cats.get(x)[z])) {
							s = x;
							break;
						}
					}
					if(s != -1) {
						break;
					}
				}
				Feature f = new Feature(inC[i], c, s);
				categories.add(f);
			}
			maxCat++;
			c++;
		}
		
		scan.close(); */
		
		Collections.sort(categories);
		
		features = new Double[cats.size()];
		
		String[] quantities = {"pound", "lb", "ounce", "oz", "ounces"};
		String[] volumes = {"cup", "quart", "tbsp", "tablespoon", "teaspoon", "cups", "quarts", "quart", "tbsps", "tablespoons", "teaspoons", "stick"};
		
		measures.addAll(Arrays.asList(quantities));
		measures.addAll(Arrays.asList(volumes));
		
		vol.addAll(Arrays.asList(volumes));
		
		//Density per CUP
		String[] thing = {"lentil", "lentils", "stock", "lemon", "lemons", "salt", "oil", "cream", "pepper", "nutmeg", "butter", "flour", "alcohol", "currant", "vinegar", "chicken broth", "thyme", "sugar", "orange", "lime", "basil", "mustard", "parsley", "cheese", "sauce", "rice",
				"tomato", "turkey", "grain", "veg", "cream", "fruit", "herb", "spice", "nut", "sweet", "meat", "seafood", "olive oil", "whipping cream", "dairy"};
		Double[] dens = {202.88, 202.88, 266.29, 257.83, 257.83, 308.55, 236.7, 255.72, 120.04, 120.04, 239.87, 148.25, 248.53, 152.16, 269.46, 266.29, 40.58, 211.34, 150.0, 90.0, 70.0, 263.12, 63.4, 130.0, 240.0, 206.05, 
				253.61, 170.0, 144.77, 194.43, 255.72, 115.18, 50.72, 100.0, 144.0, 192.0, 213.59, 128.0, 185.0, 63.4, 245.0};
		
		for(int i = 0; i < thing.length; i ++) {
			density.put(thing[i], dens[i]);
		}
		
		//Weight in POUNDS
		String[] w = {"pound", "ounce", "ounces", "oz", "lb"};
		Double[] toG = {453.592, 28.3495, 28.3495, 28.3495, 453.592};
		
		for(int i = 0; i < w.length; i ++) {
			weight.put(w[i], toG[i]);
		}
		
		String[] conv = {"cup", "cups", "tablespoon", "tablespoons", "tbsp", "tbsps", "teaspoon", "teaspoons", "quart", "quarts", "stick"};
		Double[] to = {1.0, 1.0, 16.0, 16.0, 16.0, 16.0, 48.0, 48.0, 0.25, 0.25, 2.0};
		
		for(int i = 0; i < conv.length; i ++) {
			conversions.put(conv[i], to[i]);
		}
		
		String[] misc = {"stalks celery", "large carrot", "sprig thyme", "small apple", "medium apple", "large apple", "medium tomato", "small tomato", "large tomato", "sheets lavash", "head bibb lettuce", "freshly ground black pepper", "pinch of thyme", "medium onion", "bay leaves", "whole cloves", "large garlic", "large shallot", "egg", "lettuce leaves", "cracked pepper","minced fresh parsley", "french bread", "large onion", "additional minced orange", "5- to snapper", "all purpose flour", "shallot", "garlic",
				"large mushroom", "small mushroom", "medium mushroom", "baby artichoke", "small onion", "8-ounce clam juice", "onion", "whipping cream"};
		Double[] miscW = {53.15, 73.0, 2.4, 149.0, 182.0, 223.0, 123.0, 91.0, 182.0, 76.0, 163.0, 0.7, 0.1, 110.0, 0.6, 30.0, 16.0, 14.17, 65.0, 24.0, 0.1, 
				1.2, 250.0, 340.194, 100.0, 623.69, 150.0, 14.17, 10.0, 23.0, 10.0, 18.0, 81.0, 113.398, 226.796, 169.07, 63.4};
		
		for(int i = 0; i < misc.length; i ++) {
			miscConv.put(misc[i], miscW[i]);
		}
		
		//parseJSON();
		//formatJSON();
//		testConvert();
//		printIngred();
		appendData(true);
		shuffleData();
		splitData(false);
//		makeRanks();
		//checkData();
//		RSME();
		generateCSV();
		printDist();
	}
	
	private static void printDist() throws FileNotFoundException {
		File data = new File("shuffledData.txt");
		Scanner scan = new Scanner(data);
		
		int[] dist = new int[maxCat];
		
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] arr = line.split(" ");
			int classif = (int)Double.parseDouble(arr[0]);
			dist[classif]++;
		}
		
		int total = 0;
		
		for(int i = 0; i < dist.length; i ++) {
			total += dist[i];
		}
		
		for(int i = 0; i < dist.length; i ++) {
			if(dist[i] != 0) {
				System.out.println(i + ": " + dist[i] + " " + (dist[i]*100.0/(double)total));
			}
		}
		
		scan.close();
	}
	
	private static void makeRanks() throws IOException {
		File file = new File("train.txt");
		File file2 = new File("test.txt");
		
		int num1 = 0;
		int num2 = 0;
		
		PrintWriter out = new PrintWriter(new FileWriter("rankTrain.txt"));
		PrintWriter out2 = new PrintWriter(new FileWriter("rankTest.txt"));
		
		int z = 0;
		
		Scanner scan = new Scanner(file);
		while(scan.hasNextLine()) {
			String line1 = scan.nextLine();
			Scanner scan2 = new Scanner(file);
			z++;
			for(int x = 0; x < z; x ++) {
				scan2.nextLine();
			}
			while(scan2.hasNextLine()) {
				String line2 = scan2.nextLine();
				String[] arr1 = line1.split(" ");
				String[] arr2 = line2.split(" ");
				int first = (int)Double.parseDouble(arr1[0]);
				int second = (int)Double.parseDouble(arr2[0]);
				
				String vector = "";
				if(first > second) {
					vector += "-1 ";
				} else if(first == second) {
					vector += "0 ";
				} else {
					vector += "1 ";
				}
				
				for(int i = 1; i < arr1.length; i++) {
					vector += arr1[i] + " ";
				}
				
				for(int i = 1; i < arr2.length; i++) {
					String[] f = arr2[i].split(":");
					vector += (i + maxCat) + ":" + f[1] + " ";
				}
				
				System.out.println(arr1.length + " " + arr2.length);
				System.out.println(line1);
				System.out.println(line1.split(" ").length);
				
				vector = vector.trim();
				
				out.println(vector);
				num1++;
				System.out.println("1 " + num1);
			}
			scan2.close();
		}
		scan.close();
		
		z = 0;
		
		scan = new Scanner(file2);
		while(scan.hasNextLine()) {
			String line1 = scan.nextLine();
			Scanner scan2 = new Scanner(file2);
			z++;
			for(int x = 0; x < z; x ++) {
				scan2.nextLine();
			}
			while(scan2.hasNextLine()) {
				String line2 = scan2.nextLine();
				String[] arr1 = line1.split(" ");
				String[] arr2 = line2.split(" ");
				int first = (int)Double.parseDouble(arr1[0]);
				int second = (int)Double.parseDouble(arr2[0]);
				
				String vector = "";
				if(first > second) {
					vector += "-1 ";
				} else if(first == second) {
					vector += "0 ";
				} else {
					vector += "1 ";
				}
				
				for(int i = 1; i < arr1.length; i++) {
					vector += arr1[i] + " ";
				}
				
				for(int i = 1; i < arr2.length; i++) {
					String[] f = arr2[i].split(":");
					vector += (i + maxCat) + ":" + f[1] + " ";
				}
				
				vector = vector.trim();
				
				out2.println(vector);
				num2++;
				System.out.println("2 " + num2);
			}
			scan2.close();
		}
		scan.close();
		
		out.close();
		out2.close();
	}
	
	private static void generateCSV() throws IOException {
		File file = new File("train.txt");
		File file2 = new File("test.txt");
		
		Scanner train = new Scanner(file);
		Scanner test = new Scanner(file2);
		
		PrintWriter trainOut = new PrintWriter(new FileWriter("recipe_training.csv"));
		PrintWriter testOut = new PrintWriter(new FileWriter("recipe_test.csv"));
		
		int nums = 0;
		int len = 0;
		Scanner trainNum = new Scanner(file);
		while(trainNum.hasNextLine()) {
			String line = trainNum.nextLine();
			String[] arr = line.split(" ");
			len = arr.length - 1;
			nums++;
		}
		trainNum.close();
		trainOut.println(nums + "," + len);
		
		int nums2 = 0;
		Scanner testNum = new Scanner(file2);
		while(testNum.hasNextLine()) {
			testNum.nextLine();
			nums2++;
		}
		testNum.close();
		testOut.println(nums2 + "," + len);
		
		while(train.hasNextLine()) {
			String line = train.nextLine();
			String[] arr = line.split(" ");
			String output = "";
			for(int i = 1; i < arr.length; i++) {
				String[] f = arr[i].split(":");
				output +=  f[1] + ",";
			}
			int rank = (int)Double.parseDouble(arr[0]);
			trainOut.println(output + rank);
		}
		
		while(test.hasNextLine()) {
			String line = test.nextLine();
			String[] arr = line.split(" ");
			String output = "";
			for(int i = 1; i < arr.length; i++) {
				String[] f = arr[i].split(":");
				output +=  f[1] + ",";
			}
			int rank = (int)Double.parseDouble(arr[0]);
			testOut.println(output + rank);
		}
		
		train.close();
		test.close();
		trainOut.close();
		testOut.close();
	}
	
	private static void checkData() throws FileNotFoundException {
		File file = new File("test.scaled.txt");
		File file2 = new File("train.scaled.txt");
		
		Scanner scan = new Scanner(file);
		Scanner scan2 = new Scanner(file);
		
		Scanner scan21 = new Scanner(file2);
		Scanner scan22 = new Scanner(file2);
		
		while(scan.hasNextLine()) {
			String line1 = scan.nextLine();
			while(scan21.hasNextLine()) {
				String line2 = scan21.nextLine();
				if(line2.equals(line1)) {
					System.out.println("CRUSH DREAMS");
					System.out.println(line1);
				}
			}
		}
		
		scan.close();
		scan2.close();
		
		/*while(scan21.hasNextLine()) {
			String line1 = scan21.nextLine();
			while(scan22.hasNextLine()) {
				String line2 = scan22.nextLine();
				if(line2.equals(line1)) {
					System.out.println("CRUSH DREAMS2");
					System.out.println(line1);
				}
			}
		}
		
		scan21.close();
		scan22.close();*/
		
	}
	
	private static void shuffleData() throws IOException {
		File data = new File("appendedData.txt");
		Scanner scan = new Scanner(data);
		int lines = 0;
		
		while(scan.hasNextLine()) {
			scan.nextLine();
			lines++;
		}
		scan.close();
		
		String[] file = new String[lines];
		scan = new Scanner(data);
		
		int i = 0;
		while(scan.hasNextLine()) {
			file[i] = scan.nextLine();
			i++;
		}
		
		for(i = 0; i < file.length; i ++) {
			int pos = (int)Math.floor((Math.random() * file.length));
			String temp = file[i];
			file[i] = file[pos];
			file[pos] = temp;
		}
		
		scan.close();
		
		PrintWriter out = new PrintWriter(new FileWriter("shuffledData.txt"));
		
		for(i = 0; i < file.length; i ++) {
			out.println(file[i]);
		}
		
		out.close();
	}
	
	private static void splitData(boolean even) throws IOException {
		PrintWriter train = new PrintWriter(new FileWriter("train.txt"));
		PrintWriter test = new PrintWriter(new FileWriter("test.txt"));
		
		File data = new File("shuffledData.txt");
		Scanner scan = new Scanner(data);
		
		int lines = 0;
		while(scan.hasNextLine()) {
			scan.nextLine();
			lines++;
		}
		
		scan.close();
		
		Scanner scan2 = new Scanner(data);
		
		int[] typeTrain = new int[11];
		int[] typeTest = new int[11];
		
		if(!even) {
		
			for(int i = 0; i < lines; i ++) {
				String text = scan2.nextLine();
				
				String[] arr = text.split(" ");
				int type = (int)Double.parseDouble(arr[0]);
				if(i <= (lines * 0.9)) {
					typeTrain[type]++;
					train.println(text);
				} else {
					typeTest[type]++;
					test.println(text);
				}
			}
			
		} else {
			
			for(int i = 0; i < lines; i++) {
				String text = scan2.nextLine();
				
				String[] arr = text.split(" ");
				double index = Double.parseDouble(arr[0]);
				int type = 2;
				if(index > 4) {
					type = 5;
				} else if(index > 3) {
					type = 4;
				} else if(index > 2) {
					type = 3;
				} else if(index > 1) {
					type = 2;
				}
				type = (int)Math.round(index);
				if(typeTrain[type] < 1800) {
					typeTrain[type]++;
					train.println(text);
				} else if(typeTest[type] < 200) {
					typeTest[type]++;
					test.println(text);
				}
			}
		}
		
		System.out.println("Training Dist");
		
		double totalTrain = 0;
		for(int i = 0; i < typeTrain.length; i++) {
			totalTrain += typeTrain[i];
		}
		
		for(int i = 0; i < typeTrain.length; i++) {
			if(typeTrain[i] > 0)
			System.out.println(i + " " + (typeTrain[i]/totalTrain) + " " + typeTrain[i]);
		}
		
		System.out.println("Testing Dist");
		
		double totalTest = 0;
		for(int i = 0; i < typeTest.length; i ++) {
			totalTest += typeTest[i];
		}
		
		for(int i = 0; i < typeTest.length; i ++) {
			if(typeTrain[i] > 0)
			System.out.println(i + " " + (typeTest[i]/totalTest));
		}
		
		scan2.close();
		train.close();
		test.close();
	}
	
	private static void RSME() throws FileNotFoundException {
		double error = 0;
		File file = new File("test.prediction.txt");
		File file2 = new File("test.txt");
		
		Scanner testP = new Scanner(file);
		Scanner testF = new Scanner(file2);
		
		double correct = 0;
		double total = 0;
		
		while(testF.hasNextLine()) {
			String text = testF.nextLine();
			
			String[] arr = text.split(" ");
			double guess = Double.parseDouble(testP.nextLine());
			double classif = (Double.parseDouble(arr[0]));
			
			System.out.println(guess + " " + classif);
			
			if(guess == classif) {
				correct++;
			}
			
			//error += Math.pow((guess - classif), 2);
			error += Math.abs(guess - classif);
			total ++;
		}
		System.out.println(error);
		System.out.println(error/total);
		System.out.println(((double)correct / total) * 100.0);
		
		
		testP.close();
		testF.close();
	}
	
	private static boolean isNumber(String num) {
		//System.out.println(num);
		if(num.indexOf("/") > -1 && num.split("/").length > 0) {
			String[] arr = num.split("/");
			try {
			    Integer.parseInt(arr[0]);
			  } catch (NumberFormatException e) {
			    return false;
			  }
			if(arr.length >= 2) {
				try {
					Integer.parseInt(arr[1]);
				} catch (NumberFormatException e) {
					return false;
				}
			}
		} else {
			try {
			    Integer.parseInt(num);
			  } catch (NumberFormatException e) {
			    return false;
			  }
		}
		return true;
	}
	
	private static void printIngred() throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter("C:\\Users\\Victor\\Desktop\\ingredients.txt"));
		for(int i = 0; i < cats.size(); i ++) {
			String line = "";
			for(int c = 0; c < cats.get(i).length; c++) {
				line += cats.get(i)[c] + ",";
			}
			line = line.substring(0, line.length() - 1);
			out.println(line);
		}
		out.close();
	}
	
	private static void testConvert() throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter("hey.txt"));
		int empty = 0;
		for(int i = 1; i <= 20110; i ++) {
			String path = "C:\\Users\\Victor\\Desktop\\Recipes\\Recipes_RAW\\JSON\\recipe" + i + ".txt";
			File file = new File(path);
			Scanner scan = new Scanner(file);
			
			Map<String, Object> recipe = new HashMap<String, Object>();
			
			String category = "";
			String add = "";
			while(scan.hasNext()) {
				String current = scan.next();
				if(current.endsWith("\":")) {
					if(!category.equals("") && !add.equals("")) {
						while(category.startsWith("{") || category.startsWith("\"")) {
							category = category.substring(1);
						}
						while(category.endsWith(":") || category.endsWith("\"")) {
							category = category.substring(0, category.length()-1);
						}
						add = add.trim();
						while(add.endsWith(",") || add.endsWith(" ") || add.endsWith("}")) {
							add = add.substring(0 , add.length() - 1);
						}
						if(add.startsWith("[\"") && add.endsWith("\"]")) {
							add = add.substring(2, add.length() - 2);
							recipe.put(category, add.split("\", \""));
						} else {
							while(add.startsWith("\"")) {
								add = add.substring(1);
							}
							while(add.endsWith(",") || add.endsWith("}") || add.endsWith(" ") || add.endsWith("\"")) {
								add = add.substring(0 , add.length() - 1);
							}
							if(category.equals("ingredients")) {
								String[] arr = {add};
								recipe.put(category, arr);
							} else {
								recipe.put(category, add);
							}
						}
					}
					category = current;
					add = "";
				} else {
					add += current + " ";
				}
			}
			while(category.startsWith("{") || category.startsWith("\"")) {
				category = category.substring(1);
			}
			while(category.endsWith(":") || category.endsWith("\"")) {
				category = category.substring(0, category.length()-1);
			}
			add = add.trim();
			while(add.endsWith(",") || add.endsWith(" ") || add.endsWith("}")) {
				add = add.substring(0 , add.length() - 1);
			}
			if(add.startsWith("[\"") && add.endsWith("\"]")) {
				add = add.substring(2, add.length() - 2);
				recipe.put(category, add.split("\", \""));
			} else {
				while(add.startsWith("\"")) {
					add = add.substring(1);
				}
				while(add.endsWith(",") || add.endsWith("}") || add.endsWith(" ") || add.endsWith("\"")) {
					add = add.substring(0 , add.length() - 1);
				}
				if(category.equals("ingredients")) {
					String[] arr = {add};
					recipe.put(category, arr);
				} else {
					recipe.put(category, add);
				}
			}
			
			//System.out.println(recipe.toString());
			String[] arr = (String[]) recipe.get("ingredients");
			
			features = new Double[maxCat];
			for(int f = 0; f < features.length; f ++) {
				features[f] = 0.0;
			}
			
			for(int a = 0; a < arr.length; a++) {
				boolean found = false;
				String line = arr[a];
				
				//System.out.println(line);
				if(line.toLowerCase().contains("equipment")) {
					continue;
				}
				for(int c = 0; c < categories.size(); c ++) {
						String keyword = categories.get(c).getName();
						int pos = categories.get(c).getFeature();
						if(line.toLowerCase().indexOf(keyword) > -1) {
							double grams = getGrams(line.toLowerCase(), keyword);
							if(grams == 0.0) {
								notConv++;
								//System.out.println(line + " | " + keyword);
								features[pos] = 1.0;
							} else {
								//features[pos]++;
								features[pos] = grams;
							}
							found = true;
							break;
						}
				}
				if(!found) {
					notDone++;
				}
			}
			
			String vector = "";
			
			double fTotal = 0;
			for(int v = 0; v < features.length; v++) {
				fTotal += features[v];
			}
			
			if(fTotal == 0.0) {
				System.out.println(recipe.toString());
//				unuseable++;
//				continue;
			}
			
			for(int v = 0; v < features.length; v++) {
				if(fTotal > 0) {
						features[v] /= fTotal;
				}
			}
			
			for(int v = 0; v < features.length; v++) {
				vector += features[v] + ",";
			}
			vector = vector.trim();
			vector = vector.substring(0, vector.length() - 1);
			
			double classif = 0;
					
			if(!recipe.get("rating").equals("null")) {
				classif = (int) Math.round((Double.parseDouble(recipe.get("rating").toString())) );
//				classif = (Double.parseDouble(recipe.get("rating").toString()));
			} else {
				unuseable++;
				continue;
			}
				
/*			while(classif < 2) {
				classif += 1;
			}*/
			
			//classif = classif - 2;
			
			vector = classif + " " + vector;
			//System.out.println(vector);
			if(recipe.get("rating").equals("0.0")) {
				empty++;
			}
			
			out.println(vector);
			
			scan.close();
		}
		out.close();
		System.out.println(cats.size());
		System.out.println(notDone);
		System.out.println(notConv);
		System.out.println(empty);
		System.out.println("Not using " + unuseable);
	}
	
	private static void appendData(boolean addDir) throws IOException, InterruptedException {
		File data = new File("hey.txt");
		Scanner scan = new Scanner(data);
		File dirData = new File("recipes_directions");
		Scanner dir = new Scanner(dirData);
		
		PrintWriter out = new PrintWriter(new FileWriter("appendedData.txt"));
		
		int dirLine = 0;
		while(scan.hasNextLine()) {
			String text = scan.nextLine();
			String dirText = dir.nextLine();
			//System.out.println(dirText);
			dirText = dirText.substring(2);
			String[] arr = text.split(" ");
			dirLine++;
		
			boolean allZero = true;
			String[] checkZero = arr[1].split(",");
			for(int i = 0; i < checkZero.length; i++) {
				if(Double.parseDouble(checkZero[i]) != 0.0) {
					allZero = false;
					break;
				}
			}
			
			if(!allZero) {
				if(addDir) {
					String[] left = text.split(",");
					String[] right = dirText.split(",");
					maxCat = left.length + right.length;
					
					if(left.length + right.length > 89) {
						System.out.println("NO");
						System.out.println(left.length);
						System.out.println(right.length);
						System.out.println(dirText);
						System.out.println(dirLine);
						System.exit(1);
					}
					text = text + "," + dirText;
				}
				arr = text.split(" ");
				String vector = "";
				String[] vec = arr[1].split(",");
				
				for(int i = 0; i < vec.length; i ++) {
					vector += (i + 1) + ":" + vec[i] + " ";
				}
				vector = vector.trim();
				vector = arr[0] + " " + vector;
				out.println(vector);
			}
		}
		
		out.close();
		
		dir.close();
		scan.close();
	}
	
	private static double getGrams(String line, String keyword) {
		double grams = 0.0;
		String[] pieces = line.split(" ");
		
	/*
	 * HANDLE FORMATS OF (NUMBER MEASUREMENT)
	 */
		if(pieces.length >= 2 && measures.contains(pieces[1]) && isNumber(pieces[0])) {
			//System.out.println(pieces[0] + " " + pieces[1] + " " + line.substring(index, index + keyword.length()));
			
			String[] arr = pieces[0].split("/");
			Double denom = 0.0;
			if(arr.length < 2) {
				denom = 1.0;
			} else {
				denom = Double.parseDouble(arr[1]);
			}
			Double amt = (Double.parseDouble(arr[0])/denom);
			
			if(vol.contains(pieces[1])) {
				if (density.get(keyword) != null) {
					if(conversions.get(pieces[1]) != null) {
						grams = amt * density.get(keyword) / conversions.get(pieces[1]);
					}
				}
			} else {
				if(weight.get(pieces[1]) != null) {
					grams = amt * weight.get(pieces[1]);
				}
			}
	/*
	 * HANDLE FORMATS OF (NUMBER NUMBER MEASUREMENT)
	 */
		} else if(pieces.length >= 3 && measures.contains(pieces[2]) && isNumber(pieces[0]) && isNumber(pieces[1])) {
			//System.out.println(pieces[0] + " " + pieces[1] + " " + pieces[2] + " " + line.substring(index, index + keyword.length()));
			
			String[] arr1 = pieces[0].split("/");
			Double denom1 = 0.0;
			if(arr1.length < 2) {
				denom1 = 1.0;
			} else {
				denom1 = Double.parseDouble(arr1[1]);
			}
			
			String[] arr2 = pieces[1].split("/");
			Double denom2 = 0.0;
			if(arr2.length < 2) {
				denom2 = 1.0;
			} else {
				denom2 = Double.parseDouble(arr2[1]);
			}
			Double amt = (Double.parseDouble(arr1[0])/denom1) + (Double.parseDouble(arr2[0])/denom2);
			
			if(vol.contains(pieces[2])) {
				if (density.get(keyword) != null) {
					if(conversions.get(pieces[2]) != null) {
						grams = amt * density.get(keyword) / conversions.get(pieces[2]);
					}
				}
			} else {
				if(weight.get(pieces[2]) != null) {
					grams = amt * weight.get(pieces[2]);
				}
			}
			
	/*
	 * HANDLE ARBITRARY MEASURE OF TYPE (NUMBER TYPEIECES TYPEPIECES)
	 */
		} else if(pieces.length >= 3 && isNumber(pieces[0]) && !isNumber(pieces[1]) && !isNumber(pieces[2]) && miscConv.get(pieces[1] + " " + pieces[2] + " " + keyword) != null) {
			String[] arr = pieces[0].split("/");
			Double denom = 0.0;
			if(arr.length < 2) {
			denom = 1.0;
		} else {
			denom = Double.parseDouble(arr[1]);
		}
		Double amt = Double.parseDouble(arr[0])/denom;
			
		grams = amt * miscConv.get(pieces[1] + " " + pieces[2] + " " + keyword);
	/*
	 * HANDLE ARBITRARY MEASURE OF TYPE (NUMBER NUMBER TYPEPIECES)
	 */
		} else if(pieces.length >= 3 && isNumber(pieces[0]) && isNumber(pieces[1]) && !isNumber(pieces[2]) && miscConv.get(pieces[2] + " " + keyword) != null) {
			String[] arr = pieces[1].split("/");
			Double denom = 0.0;
			if(arr.length < 2) {
				denom = 1.0;
			} else {
				denom = Double.parseDouble(arr[1]);
			}
			Double amt = Double.parseDouble(pieces[0]) + (Double.parseDouble(arr[0])/denom);
			
			grams = amt * miscConv.get(pieces[2] + " " + keyword);
	/*
	 * HANDLE ARBITRARY MEASURE OF TYPE (TYPEPIECES TYPEPIECES)
	 */
		} else if(pieces.length >= 2 && !isNumber(pieces[0]) && !isNumber(pieces[1]) && miscConv.get(pieces[0] + " " + pieces[1] + " " + keyword) != null) {
			grams =  miscConv.get(pieces[0] + " " + pieces[1] + " " + keyword);
			
	/*
	 * HANDLE ARBITRARY MEASURE OF TYPE (NUMBER TYPEPIECES)
	 */
		} else if(pieces.length >= 3 && isNumber(pieces[0]) && !isNumber(pieces[1]) && miscConv.get(pieces[1] + " " + keyword) != null) {
			
			String[] arr = pieces[0].split("/");
			Double denom = 0.0;
			if(arr.length < 2) {
				denom = 1.0;
			} else {
				denom = Double.parseDouble(arr[1]);
			}
			Double amt = (Double.parseDouble(arr[0])/denom);
			
			grams =  amt * miscConv.get(pieces[1] + " " + keyword);
			
	/*
	 * HANDLE ARBITRARY MEASURE OF TYPE (TYPEPIECES)
	 */
		} else if(pieces.length >= 1 && !isNumber(pieces[0]) && miscConv.get(pieces[0] + " " + keyword) != null) {
			grams =  2 * miscConv.get(pieces[0] + " " + keyword);
		} else if(pieces.length >= 2 && !isNumber(pieces[1]) && miscConv.get(keyword + " " + pieces[1]) != null) {
			grams =  2 * miscConv.get(keyword + " " + pieces[1]);	
		} else if(density.get(keyword) != null) {
			grams =  0.25 *density.get(keyword);
			
	/*
	 * HANDLE ARBITRARY MEASURE OF TYPE (NUMBER)
	 */
		} else if(pieces.length >= 2 && isNumber(pieces[0]) && !isNumber(pieces[1]) && miscConv.get(keyword) != null) {
			String[] arr = pieces[0].split("/");
			Double denom = 0.0;
			if(arr.length < 2) {
				denom = 1.0;
			} else {
				denom = Double.parseDouble(arr[1]);
			}
			Double amt = (Double.parseDouble(arr[0])/denom);
			
			grams = amt * miscConv.get(keyword);
		}
		
		if(grams == 0.0) {
		
			int which = -1;
			
			for(int c = 0; c < categories.size(); c++) {
				Feature f = categories.get(c);
				if(f.getName().equals(keyword)) {
					which = f.getSuper();
					break;
				}
			}
			
			if(which == -1) {
				System.out.println("-1 " + keyword);
			}
			
			if(which >= 0 && which <= 20) {
//				System.out.println("falling back on veg");
				grams = getGrams(line, "veg");
			}
			if(which >= 21 && which <= 31) {
//				System.out.println("falling back on grain");
				grams = getGrams(line, "grain");
			}
			if(which >= 32 && which <= 35) {
//				System.out.println("falling back on dairy");
				grams = getGrams(line, "dairy");
			}
			if(which >= 36 && which <= 38) {
//				System.out.println("falling back on cheese");
				grams = getGrams(line, "cheese");
			}
			if(which >= 39 && which <= 40) {
//				System.out.println("falling back on fruit");
				grams = getGrams(line, "fruit");
			}
			if(which >= 41 && which <= 45) {
//				System.out.println("falling back on herbs");
				grams = getGrams(line, "herb");
			}
			if(which >= 46 && which <= 52) {
//				System.out.println("falling back on sauce");
				grams = getGrams(line, "sauce");
			}
			if(which >= 53 && which <= 59) {
//				System.out.println("falling back on spice");
				grams = getGrams(line, "spice");
			}
			if(which == 60) {
//				System.out.println("falling back on nut");
				grams = getGrams(line, "nut");
			}
			if(which >= 61 && which <= 64) {
//				System.out.println("falling back on sweet");
				grams = getGrams(line, "sweet");
			}
			if(which >= 65 && which <= 67) {
//				System.out.println("falling back on meat");
				grams = getGrams(line, "meat");
			}
			if(which >= 68 && which <= 71) {
//				System.out.println("falling back on seafood");
				grams = getGrams(line, "seafood");
			}
			if(which == 72) {
//				System.out.println("falling back on alcohol");
				grams = getGrams(line, "alcohol");
			}
		
		}
		
		return grams;
	}
	
	private static void formatJSON() throws IOException {
		for(int i = 0; i <= 20110; i ++) {
			String path = "C:\\Users\\Victor\\Desktop\\Recipes\\Recipes_RAW\\JSON\\recipe" + i + ".txt";
			File file = new File(path);
			Scanner scan = new Scanner(file);
			
			String data = scan.nextLine();
			if(!data.equals("")) {
				while(!data.startsWith("{")) {
					data = data.substring(1);
				}
				while(!data.endsWith("}")) {
					data = (data.substring(0, data.length() - 1));
					System.out.println(data);
				}
				PrintWriter out = new PrintWriter(new FileWriter(path));
				out.println(data);
				out.close();
			}
				scan.close();
		}
	}
	
	private static void parseJSON() throws IOException {
		File recipes = new File("C:\\Users\\Victor\\Desktop\\Recipes\\Recipes_RAW\\full_format_recipes.JSON");
		Scanner scan = new Scanner(recipes);
		String JSON = "";
		int count = -1;
		while(scan.hasNext()) {
			String add = scan.next();
			if(add.indexOf("\"directions\":") >= 0) {
				count++;
				PrintWriter out = new PrintWriter(new FileWriter("C:\\Users\\Victor\\Desktop\\Recipes\\Recipes_RAW\\JSON\\recipe" + count + ".txt"));
				JSON.replace(", {}", "");
				out.println(JSON);
				out.close();
				System.out.println(count);
				JSON = "";
			}
			JSON += add;
			if(scan.hasNext()) {
				JSON += " ";
			}
		}
		
		System.out.println(JSON);
		
		scan.close();
	}
	
	private static void readKeys() throws FileNotFoundException {
		File keyFile = new File("C:/Users/Victor/Desktop/apiKeys.txt");
		Scanner scan = new Scanner(keyFile);
		
		String key = "";
		while(scan.hasNextLine()) {
			key = scan.nextLine();
			keys.add(key);
			System.out.println(key);
		}
		scan.close();
	}

	private static void sendGET(String GET_URL, int iter) throws IOException {
		URL obj = new URL(GET_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			String output = response.toString();
			System.out.println(output);
			
			if(false) {
				PrintWriter out = new PrintWriter(new FileWriter(FILE_PATH + "/recipe" + iter + ".txt", true));
				out.println(output);
				out.close();
			}
			System.out.println("Finished iter " + iter);
		} else {
			System.out.println("GET request not worked");
		}

	}
	
}
