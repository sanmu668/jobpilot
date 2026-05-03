import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    profile: null,
    resumes: [],
    currentResumeId: null
  }),

  getters: {
    currentResume: (state) =>
      state.resumes.find(r => r.id === state.currentResumeId) || state.resumes[0] || null
  },

  actions: {
    setProfile(profile) {
      this.profile = profile
    },
    setResumes(resumes) {
      this.resumes = resumes
    },
    setCurrentResume(id) {
      this.currentResumeId = id
    },
    addResume(resume) {
      this.resumes.unshift(resume)
    }
  }
})
