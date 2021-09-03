import {get, postJson,del} from '@/utils/axios'

export default {
    getTrees(id) {
        return get(`/admin/${id}/trees`);
    },
    getTopics(id) {
        return get(`/admin/${id}/topics`);
    },
    createTopic(id, topic) {
        return postJson(`/admin/${id}/topic/create`, topic);
    },
    deleteTopic(id,topic){
        return del(`/admin/${id}/${topic}`)
    }
}