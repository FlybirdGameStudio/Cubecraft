package ink.flybird.cubecraft.client.render.model;

import ink.flybird.cubecraft.client.ClientRenderContext;
import ink.flybird.cubecraft.client.render.model.object.Model;
import ink.flybird.cubecraft.client.resources.Resource;
import ink.flybird.cubecraft.client.resources.ResourceLocation;
import ink.flybird.fcommon.logging.Logger;
import ink.flybird.fcommon.logging.SimpleLogger;
import io.flybird.cubecraft.register.SharedContext;

import java.util.HashMap;

public class ModelManager <I extends Model>{
    private final Class<I> clazz;
    private final ResourceLocation fallback;
    private final Logger logger=new SimpleLogger("ModelManager");

    public ModelManager(Class<I> clazz,ResourceLocation fallback){
        this.clazz = clazz;
        this.fallback=fallback;
    }

    private final HashMap<String,I> models =new HashMap<>();

    public I get(String id) {
        return models.get(id);
    }

    public void load(String file){
        Resource res;
        try{
            res= ClientRenderContext.RESOURCE_MANAGER.getResource(file);
        }catch (Exception e){
            res= ClientRenderContext.RESOURCE_MANAGER.getResource(fallback);
        }

        String json= res.getAsText();
        try {
            I model = SharedContext.createJsonReader().fromJson(json, clazz);
            this.models.put(file,model);
        }catch (Exception e){
            I model = SharedContext.createJsonReader().fromJson(ClientRenderContext.RESOURCE_MANAGER.getResource(fallback).getAsText(), clazz);
            this.models.put(file,model);
            this.logger.exception(e);
        }
    }

    public void load(ResourceLocation loc){
        load(loc.format());
    }
}