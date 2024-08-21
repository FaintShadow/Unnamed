package game.engine.interfaces;

import game.engine.Position;
import game.engine.concerns.ChunkGenerationException;

public interface Chunkable<T>{
    public T generateChunk(Position position) throws ChunkGenerationException;
}
