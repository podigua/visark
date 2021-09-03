import {postJson} from "./axios";

export default {
    open(params) {
        return postJson("/window/open", params);
    }
}