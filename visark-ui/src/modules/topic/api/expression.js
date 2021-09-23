import {get} from "@/utils/axios";

export default {
    getFunctions(){
        return get('/expression/functions')
    }
}