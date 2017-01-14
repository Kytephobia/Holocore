package network.packets.swg.zone.object_controller;

import java.nio.ByteBuffer;

public class MessageQueueDraftSchematics extends ObjectController {

	private final static int CRC = 0x0102;
	
	private long toolId;
	private long craftingStationId;
	private int schematicsCounter;
	private int schematicsId;
	private int schematicsCrc;
	private byte[] subcategory;

	public MessageQueueDraftSchematics(long toolId, long craftingStationId, int schematicsCounter, int schematicsId, int schematicsCrc, byte[] subcategory) {
		super(CRC);
		this.toolId = toolId;
		this.craftingStationId = craftingStationId;
		this.schematicsCounter = schematicsCounter;
		this.schematicsId = schematicsId;
		this.schematicsCrc = schematicsCrc;
		System.arraycopy(subcategory, 0, this.subcategory, 0, 4);
	}

	public MessageQueueDraftSchematics(ByteBuffer data) {
		super(CRC);
		decode(data);
	}
	
	@Override
	public void decode(ByteBuffer data) {
		decodeHeader(data);
		toolId = getLong(data);
		craftingStationId = getLong(data);
		schematicsCounter = getInt(data);
		for(int i = 0; i < schematicsCounter; i++){
			schematicsId = getInt(data);
			schematicsCrc = getInt(data);
			subcategory = getArray(data);
		}		
	}

	@Override
	public ByteBuffer encode() {
		ByteBuffer data = ByteBuffer.allocate(HEADER_LENGTH + 20 + schematicsCounter * 12);
		encodeHeader(data);
		addLong(data, toolId);
		addLong(data, craftingStationId);
		addInt(data, schematicsCounter);
		for(int i = 0; i< schematicsCounter; i++){
			addInt(data, schematicsId);
			addInt(data, schematicsCrc);
			addData(data, subcategory);
		}
		return data;
	}

	public long getToolId() {
		return toolId;
	}

	public void setToolId(long toolId) {
		this.toolId = toolId;
	}

	public long getCraftingStationId() {
		return craftingStationId;
	}

	public void setCraftingStationId(long craftingStationId) {
		this.craftingStationId = craftingStationId;
	}

	public int getSchematicsCounter() {
		return schematicsCounter;
	}

	public void setSchematicsCounter(int schematicsCounter) {
		this.schematicsCounter = schematicsCounter;
	}

	public int getSchematicsId() {
		return schematicsId;
	}

	public void setSchematicsId(int schematicsId) {
		this.schematicsId = schematicsId;
	}

	public int getSchematicsCrc() {
		return schematicsCrc;
	}

	public void setSchematicsCrc(int schematicsCrc) {
		this.schematicsCrc = schematicsCrc;
	}

	public byte[] getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(byte[] subcategory) {
		this.subcategory = subcategory;
	}
}