import {get, postJson} from "@/utils/axios";

export default {
    getOption() {
        return get("/option");
    },
    getDeserializers() {
        return get("/option/deserializers");
    },
    save(params) {
        return postJson("/option", params);
    }
}