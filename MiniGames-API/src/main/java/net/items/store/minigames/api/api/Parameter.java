package net.items.store.minigames.api.api;

import lombok.Getter;

@Getter
public class Parameter {

    private ParameterType parameterType;
    private Object compare;

    protected Parameter(ParameterType parameterType){
        this.parameterType = parameterType;
        this.compare = null;
    }

    public static Parameter create(ParameterType parameterType){
        return new Parameter(parameterType);
    }

    public Parameter addCompare(Object compare){
        this.compare = compare;
        return this;
    }

    public boolean compare(Object compare){
        return this.compare != null && this.compare.equals(compare);
    }
}
