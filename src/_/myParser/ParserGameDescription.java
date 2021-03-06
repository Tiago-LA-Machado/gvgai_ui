package _.myParser;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import _._.jetty.server.LevelMatrix;
import _.utils.Utils;
import core.Node;
import core.VGDLParser;
import core.content.InteractionContent;
import core.content.MappingContent;
import core.content.SpriteContent;
import core.content.TerminationContent;
import tools.IO;

/**
 * @author Tiago Machado
 *
 * Apr 10, 2018
 */
public class ParserGameDescription extends VGDLParser{

	public ArrayList<SpriteContentParsed> spriteSet;

	public ParserGameDescription()
	{
		super();
		spriteSet = new ArrayList<>();
	}

	public ParserGameDescription(String game)
	{
		super();
		spriteSet = new ArrayList<>();
	}
	
	/**
	 * @param n
	 */
	public ArrayList<SpriteContentParsed> parseSpriteSet(Node n) {
		ArrayList<Node> spriteNodes = n.children;
		for (Node node : spriteNodes) {
			
			SpriteContent spriteContent = (SpriteContent) node.content;
			SpriteContentParsed spriteContentParsed = new SpriteContentParsed(spriteContent);

			if(node.parent.content.identifier.equals("SpriteSet"))
			{
				spriteSet.add(spriteContentParsed);
			}
			
			parseSpriteChildren(node, spriteContentParsed);
			/*********COMMENT THE LINE BELOW***************/
			spriteContentParsed.childrenInheritParameters();
			/****TO HAVE THE REGULAR VGDL PARSER**********/
			JSONObject obj = spriteContentParsed.spriteContentParsedToJSON();
		}
		return spriteSet;
	}
	
	public JSONArray parseInteractionSet(Node n)
	{
		JSONArray interactionArray = new JSONArray();
		ArrayList<Node> interactionNodes = n.children;
		for(Node node : interactionNodes)
		{
			JSONObject interactionObj = new JSONObject();
			InteractionContent interactionContent = (InteractionContent) node.content;
			interactionObj.put("interactionName", interactionContent.function);
			interactionObj.put("sprite1", interactionContent.object1);
			JSONArray sprite2Array = new JSONArray();
			String [] spritesToInteract = interactionContent.object2;
			for (int i = 0; i < spritesToInteract.length; i++) {
//				JSONObject objToInteract = new JSONObject();
//				objToInteract.put("spriteToInteract", spritesToInteract[i]);
				sprite2Array.add(spritesToInteract[i]);
			}
			interactionObj.put("sprite2", sprite2Array);
			HashMap<String, String> parameters = interactionContent.parameters;
			JSONObject paramObj = new JSONObject();
			for (String key : parameters.keySet()) 
			{
				paramObj.put(key, parameters.get(key));
			}
			interactionObj.put("parameters", paramObj);
			interactionArray.add(interactionObj);
		}
		return interactionArray;
	}
	
	public JSONArray parseTerminationSet(Node n)
	{
		JSONArray terminationArray = new JSONArray();
		ArrayList<Node> terminationNodes = n.children;
		for (Node node : terminationNodes) {
			JSONObject terminationObj = new JSONObject();
			TerminationContent terminationContent = (TerminationContent) node.content;
			terminationObj.put("termination", terminationContent.identifier);
			HashMap<String, String> parameters = terminationContent.parameters;
			JSONObject paramObj = new JSONObject();
			for (String key : parameters.keySet()) 
			{
				paramObj.put(key, parameters.get(key));
			}
			terminationObj.put("parameters", paramObj);
			terminationArray.add(terminationObj);
		}
		return terminationArray;
	}
	
	public JSONObject parseLevelMappingSet(Node n)
	{
		JSONObject mappingObject = new JSONObject();
		ArrayList<Node> mappingNodes = n.children;
		for(Node node : mappingNodes)
		{
			MappingContent mappingContent = (MappingContent) node.content;
			String mappingComponent = mappingContent.line.replace(mappingContent.identifier + " > ", "");
			String [] mappedComponents = mappingComponent.split(" ");
			JSONArray mappedArray = new JSONArray();
			JSONObject mappedObject = new JSONObject();
			for(int i = 0; i < mappedComponents.length; i++)
			{
				mappedArray.add(mappedComponents[i]);
			}
			mappingObject.put(mappingContent.identifier, mappedArray);
		}
		return mappingObject;
	}

	/**
	 * @param node
	 * @param spriteContentParsed
	 */
	public void parseSpriteChildren(Node node, SpriteContentParsed spriteContentParsed) {
		if(node.children.size() > 0)
		{
			for (Node innerNode : node.children) 
			{
				SpriteContent innerSpriteContent = (SpriteContent) innerNode.content;
				SpriteContentParsed innerSpriteContentParsed = new SpriteContentParsed(innerSpriteContent);
				spriteContentParsed.addSprite(innerSpriteContentParsed);
				parseSpriteChildren(innerNode, innerSpriteContentParsed);
			}
		}
	}
	
	/**
	 * @param myParser
	 * @param n
	 */
	public JSONArray generate(Node n, String gameName) {
		JSONArray spriteJSONArray = new JSONArray();
		ArrayList<SpriteContentParsed> spriteContentParseds = this.parseSpriteSet(n);
		for (SpriteContentParsed spriteContentParsed : spriteContentParseds) {
			JSONObject obj = spriteContentParsed.exploreNode(spriteContentParsed);
			obj.put("gameItBelongsTo", gameName);
			spriteJSONArray.add(obj);
		}
		return spriteJSONArray;
	}
	
	public String getGameObject(String gamePath)
	{
		String toSend = "";
	
		JSONArray spriteSet = null;
		JSONObject levelMapping = null;
		JSONArray interactionSet = null;
		JSONArray terminationSet = null;
		JSONObject game = new JSONObject();

		ParserGameDescription parser = new ParserGameDescription();
		String[] desc_lines = new IO().readFile(gamePath);
		if(desc_lines != null)
		{
			Node rootNode = parser.indentTreeParser(desc_lines);

			//Parse here blocks of VGDL.
			for(Node n : rootNode.children)
			{
				if(n.content.identifier.equals("SpriteSet"))
				{
					String gameName = gamePath.replace(".txt", "").replace("examples/gridphysics/", "");
					spriteSet = parser.generate(n, gameName);
				}
			
				if(n.content.identifier.equals("LevelMapping"))
				{
					levelMapping = parser.parseLevelMappingSet(n);
				}
			
				if(n.content.identifier.equals("InteractionSet"))
				{
					interactionSet = parser.parseInteractionSet(n);
				}
			
				if(n.content.identifier.equals("TerminationSet"))
				{
					terminationSet = parser.parseTerminationSet(n);
				}
			}
		
			JSONObject level = getLevel(gamePath.replace(".txt", "_lvl0.txt"));
		
			game.put("SpriteSet", spriteSet);
			game.put("LevelMapping", levelMapping);
			game.put("InteractionSet", interactionSet);
			game.put("TerminationSet", terminationSet);
			game.put("Level", level);
		
			toSend = game.toJSONString();
		
		}
		return toSend;
	}
	
	public JSONObject getLevel(String gameLevelPath)
	{
		LevelMatrix levelMatrix = new LevelMatrix();
		JSONObject level = levelMatrix.getObjectLevelMatrix(gameLevelPath);
		return level;
	}

	public static void main(String [] args){
		
		ParserGameDescription myParser = new ParserGameDescription();

		String[] desc_lines = new IO().readFile("examples/gridphysics/zelda.txt");
		if(desc_lines != null)
		{
			Node rootNode = myParser.indentTreeParser(desc_lines);

			//Parse here blocks of VGDL.
			for(Node n : rootNode.children)
			{
				if(n.content.identifier.equals("SpriteSet"))
				{
					Utils.writeAsAJSON(myParser.generate(n, "zelda"), "sprtiteSet");
					System.out.println();
				}
				
				else if(n.content.identifier.equals("InteractionSet"))
				{
					JSONArray interactionJSONArray = myParser.parseInteractionSet(n);
					Utils.writeAsAJSON(interactionJSONArray, "interactionSet");
					System.out.println();
				}
				
				else if(n.content.identifier.equals("LevelMapping"))
				{

				}
				
				else if(n.content.identifier.equals("TerminationSet"))
				{
					JSONArray terminationArray = myParser.parseTerminationSet(n);
					Utils.writeAsAJSON(terminationArray, "interactionSet");
					System.out.println();
				}
			}

		}
	}
}