import {get,postJson} from '@/utils/axios'

export default {
    get(cluster,topic){
        return get(`/topic/option/${cluster}/${topic}`)
    },
    save(params){
        return postJson("/topic/option",params);
    }
}