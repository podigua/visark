import {get,postJson,del} from '@/utils/axios'
export default {
    save(params){
        return postJson("/programme",params)
    },
    query4List(cluster,topic){
        return get('/programme/list',{
            cluster:cluster,
            topic:topic
        })
    },
    deleteById(id){
        return del(`/programme/${id}`)
    }
}