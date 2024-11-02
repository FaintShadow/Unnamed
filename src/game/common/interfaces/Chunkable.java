package game.common.interfaces;

import game.core.engine.position.Position;
import game.exceptions.ChunkGenerationException;

public interface Chunkable<T>{
    public T generateChunk(Position position) throws ChunkGenerationException;
}
