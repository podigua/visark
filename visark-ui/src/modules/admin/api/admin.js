import {get, postJson,del} from '@/utils/axios'

export default {
    getTrees(id,config) {
        return get(`/admin/${id}/trees`,{},config);
    },
    getTopics(id) {
        return get(`/admin/${id}/topics`);
    },
    createTopic(id, topic) {
        return postJson(`/admin/${id}/topic/create`, topic);
    },
    deleteTopic(id,topic){
        return del(`/admin/${id}/${topic}`)
    },
    getNode(cluster,id){
        return get(`/admin/${cluster}/node/${id}`);
    },
    getNodes(cluster){
        return get(`/admin/${cluster}/nodes`);
    },
    getTopic(cluster,topic){
        return get(`/admin/${cluster}/topic/${topic}`);
    }
}