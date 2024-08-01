package game.utilities.interfaces;

import game.utilities.Position;
import game.utilities.errors.ChunkGenerationException;

import java.util.List;

public interface Chunkable<T>{
    public List<T> genChunk(Position position) throws ChunkGenerationException;
}
