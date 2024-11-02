package game.common.interfaces;

import game.core.engine.camera.AdvCamera2D;
import game.core.engine.position.Position;
import game.exceptions.ChunkGenerationException;

public interface Chunkable<T>{
    default void generateChunks(AdvCamera2D camera) throws ChunkGenerationException{}
    default void generateChunks() throws ChunkGenerationException{}
    T generateChunk(Position position) throws ChunkGenerationException;
}
